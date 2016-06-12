package adapter.http

import groovy.json.JsonSlurper

class ResponseParser {

    def static List<Map<String, String>> parseResponseGET(String response) {
        JsonSlurper jsonSlurper = new JsonSlurper();
        def list=new ArrayList();
        def jsonObj = jsonSlurper.parseText(response);
        list.add(jsonObj);
        return list
    }
}
