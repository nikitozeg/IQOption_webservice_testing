package utils

import cucumber.api.DataTable
import cucumber.runtime.table.TableDiffException

/**
 * Created by ivannik on 12/6/2016.
 */
class DataTableUtils {

    static def diff(DataTable table, List<Map<String, String>> receivedObjects) throws TableDiffException {
        try {
            table.unorderedDiff(prepareDataForDiff(table, receivedObjects))
        } catch (TableDiffException e) {
            assert false: e
        }
    }


    private static List<?> prepareDataForDiff(DataTable table, List<Map<String, String>> receivedObjects) {
        def headers = table.raw().first()
        [headers] + receivedObjects.collect { toDataTableRow(headers, it) }
    }

    static def toDataTableRow(headers, Map<String, String> object) {
        headers.collect {
            def value = object[it]
            switch (it) {
                case "result": return ""
                default: return value == null ? "null" : value.toString()
            }
        }
    }

    static def checkDetails(DataTable table, Map<String, String> receivedData) {
        def headers = table.raw().first()
        assert headers.size().equals(receivedData.size()): "Received data should contain: " + headers + ", actual: " + receivedData.keySet()
        headers.each { String header ->
            switch (header) {
                case "graph": assert !(receivedData.get(header).isEmpty()); break;
                case "percent":  break;
                default: assert table.asMaps(String, String)[0].get(header).equals(receivedData.get(header).toString()): header + " is incorrect, actual value: " + receivedData.get(header) + ", should be " + table.asMaps(String, String)[0].get(header)
            }
        }
    }

    static def checkPercent(Map<String, String> receivedData) {
        def ints = 0..100
               assert (ints.contains(receivedData.get("percent")));
    }

}
