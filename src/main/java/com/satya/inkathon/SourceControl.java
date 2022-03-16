package com.satya.inkathon;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.satya.inkathon.model.Staff;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SourceControl {
    ObjectMapper objmapper = new ObjectMapper();
    ObjectMapper xmlmapper = new XmlMapper();
    String ans;

    @PostMapping(path = "/jsonToXml")
    @ResponseBody
    public String jsonToXml(@RequestParam("formData") String json) {
        try {
            JsonNode tree = objmapper.readTree(json);
            return xmlmapper.writer().withRootName("Root").writeValueAsString(tree);

        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @PostMapping(path = "/xmlToJson")
    @ResponseBody
    public String xmlToJson(@RequestParam("formData") String xml) {
        try {
            JsonNode node = xmlmapper.readTree(xml.getBytes());
            return objmapper.writeValueAsString(node);

        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @PostMapping(path = "/jsonToPojo")
    @ResponseBody
    public String jsonToPojo(@RequestParam("formData") String json) {
        try {
            Staff staff = objmapper.readValue(json, Staff.class);
            return staff.toString();
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @PostMapping(path = "/xmlToPojo")
    @ResponseBody
    public String xmlToPojo(@RequestParam("formData") String xml) {
        try {
            JsonNode node = xmlmapper.readTree(xml.getBytes());
            String json = objmapper.writeValueAsString(node);
            Staff staff = objmapper.readValue(json, Staff.class);
            return staff.toString();
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @PostMapping(path = "/jsonDiff")
    @ResponseBody
    public String home(@RequestParam("ljson") String ljson, @RequestParam("rjson") String rjson) {
        TypeReference<HashMap<String, Object>> type = new TypeReference<HashMap<String, Object>>() {
        };

        try {
            Map<String, Object> lMap = objmapper.readValue(ljson, type);
            Map<String, Object> rMap = objmapper.readValue(rjson, type);

            MapDifference<String, Object> diff = Maps.difference(lMap, rMap);
            ans = "";

            if (diff.entriesDiffering().isEmpty() == false) {
                ans = "Entries differing\n--------------------------\n";
                diff.entriesDiffering()
                        .forEach((key, value) -> ans += (key + ": " + value + "\n"));
                ans += "\n\n";
            }

            if (diff.entriesOnlyOnLeft().isEmpty() == false) {
                ans += "Entries only on the top\n--------------------------\n";
                diff.entriesOnlyOnLeft()
                        .forEach((key, value) -> ans += (key + ": " + value + "\n"));
                ans += "\n\n";
            }

            if (diff.entriesOnlyOnRight().isEmpty() == false) {
                ans += "Entries only on the bottom\n--------------------------\n";
                diff.entriesOnlyOnRight()
                        .forEach((key, value) -> ans += (key + ": " + value + "\n"));
            }

            if (ans == "") {
                ans = "Entries differing\n--------------------------\n";
                ans += "No difference found.";
            }

            return ans;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @PostMapping(path = "/dateConvert")
    @ResponseBody
    public String dateConvert(@RequestParam("date") String inpDate, @RequestParam("type") String type)
            throws ParseException {
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter2 = new SimpleDateFormat(type);
        Date date = formatter1.parse(inpDate);
        String result = formatter2.format(date);
        return result;
    }
}