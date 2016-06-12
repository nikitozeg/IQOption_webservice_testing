package iqoption

import adapter.http.QuotationServiceAdapter
import cucumber.api.DataTable
import helpers.ResponseHelper
import utils.DataTableUtils

this.metaClass.mixin(cucumber.api.groovy.Hooks)
this.metaClass.mixin(cucumber.api.groovy.EN)

List<Map<String, String>> responseObject = null

When(~'^Web service is connected$') { ->
    QuotationServiceAdapter.instance.connect()
}

And(~'^Web service is disconnected$') { ->
    QuotationServiceAdapter.instance.disconnect()
}

And(~'^Request is executed') { ->
    responseObject = QuotationServiceAdapter.instance.searchCP("quotesnapshot", "?active_id=1")
}


Then(~'^Response object have following parameters:$') { DataTable table ->
    DataTableUtils.diff(table, responseObject)
    responseObject.each { it ->
        ResponseHelper.compareAttributes(table, it)
    }
}

And(~'^Parameter result have following parameters with values:$') { DataTable table ->
    DataTableUtils.checkDetails(table, responseObject[0].result);
}

And(~'^Parameter graph have (\\d+) elements with (\\d+) and second value on each element is less than (\\d+)$') { int elementsSize, int valuesInElementSize, int quotation ->

    assert responseObject[0].result.graph.size() == elementsSize
    responseObject[0].result.graph.each { obj ->
        assert obj.size == valuesInElementSize
        assert obj.get(1) < quotation

    }

}

And(~'^Parameter percent is in the range of 0..100 inclusively$') { ->
    DataTableUtils.checkPercent(responseObject[0].result);
}















