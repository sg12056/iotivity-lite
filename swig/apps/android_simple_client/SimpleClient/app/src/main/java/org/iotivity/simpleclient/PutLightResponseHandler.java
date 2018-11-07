package org.iotivity.simpleclient;

import org.iotivity.CborEncoder;
import org.iotivity.OCClientResponse;
import org.iotivity.OCMain;
import org.iotivity.OCQos;
import org.iotivity.OCStatus;
import org.iotivity.OCResponseHandler;

public class PutLightResponseHandler implements OCResponseHandler {

    private static final String TAG = PutLightResponseHandler.class.getSimpleName();

    private ClientActivity activity;

    public PutLightResponseHandler(ClientActivity activity) {
        this.activity = activity;
    }

    @Override
    public void handler(OCClientResponse response) {
        Light light = (Light) response.getUser_data();
        activity.msg("PUT light:");
        if (response.getCode() == OCStatus.OC_STATUS_CHANGED) {
            activity.msg("\tPUT response: CHANGED");
        } else {
            activity.msg("\tPUT response code " + response.getCode().toString() + "(" + response.getCode() + ")");
        }
        activity.printLine();

        PostLightResponseHandler postLight = new PostLightResponseHandler(activity);
        if (OCMain.initPost(light.serverUri, light.serverEndpoint, null, postLight, OCQos.LOW_QOS, light)) {
            CborEncoder root = OCMain.repBeginRootObject();
            OCMain.repSetBoolean(root, "state", false);
            OCMain.repSetInt(root, "power", 105);
            OCMain.repEndRootObject();

            if (OCMain.doPost()) {
                activity.msg("\tSent POST request");
            } else {
                activity.msg("\tCould not send POST request");
            }
        } else {
            activity.msg("\tCould not init POST request");
        }
        activity.printLine();
    }
}
