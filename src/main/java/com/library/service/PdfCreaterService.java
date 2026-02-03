package com.library.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;

import tools.jackson.databind.JsonNode;

/*
	{	
		"title":"Sample PDF Report",
		"headers":{
			"Name":"name",
			"Age":"age",
			"City":"city"
		},
		"rows":[
			{"name":"John Doe","age":"30","city":"New York"},
			{"name":"Jane Smith","age":"25","city":"Los Angeles"},
			{"name":"Mike Johnson","age":"35","city":"Chicago"}
		]
	}
*/

@Service
@Lazy
public class PdfCreaterService {

	public byte[] createPdf(JsonNode payload) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try (PdfWriter writer = new PdfWriter(out)) {
			PdfDocument pdf = new PdfDocument(writer);
			Document document = new Document(pdf);
			String title = payload.get("title").asString();
			Map<String, String> headerPathMap = new HashMap<>();
			JsonNode headerKey = payload.get("headers");
			JsonNode rows = payload.get("rows");
			document.add(new Paragraph(title));

			for (Entry<String, JsonNode> entry : headerKey.properties()) {
				headerPathMap.put(entry.getKey(), entry.getValue().asString());
			}
			Table table = new Table(UnitValue.createPercentArray(headerPathMap.keySet().size()));
			table.useAllAvailableWidth();
			headerPathMap.keySet().forEach(h -> table.addHeaderCell(new Cell().add(new Paragraph(h))));
			for (JsonNode node : rows) {
				headerPathMap.keySet().forEach(
						h -> table.addCell(new Cell().add(new Paragraph(node.get(headerPathMap.get(h)).asString()))));
			}

			document.add(table);

			document.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out.toByteArray();
	}
}
