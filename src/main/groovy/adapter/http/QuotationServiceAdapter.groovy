package adapter.http

import configuration.Configuration
/**
 * Created by ivannik on 12/06/2016.
 */

@Singleton(strict = false)

class QuotationServiceAdapter extends HttpsServiceAdapter {

    private QuotationServiceAdapter() {
        super((String) Configuration.getConf().http)
    }

    def List<Map<String, String>> searchCP(String servicePath, String value) {
        return callJsonServiceAsGET(servicePath,value, new ResponseParser())
    }




}
