package com.xgimi.gmsdk.bean.send;

import java.util.HashMap;

/**
 * Created by Tommy on 2017/9/4.
 */

public class GMKeyCommandFactory {

    public static HashMap<Integer, String> mKeyMap;
    static {
        mKeyMap = new HashMap<Integer, String>();
        mKeyMap.put(GMKeyCommand.GMKeyEventLeft, Consants.GMKeyEventLeft);
        mKeyMap.put(GMKeyCommand.GMKeyEventRight, Consants.GMKeyEventRight);
        mKeyMap.put(GMKeyCommand.GMKeyEventUp, Consants.GMKeyEventUp);
        mKeyMap.put(GMKeyCommand.GMKeyEventDown, Consants.GMKeyEventDown);
        mKeyMap.put(GMKeyCommand.GMKeyEventOk, Consants.GMKeyEventOk);
        mKeyMap.put(GMKeyCommand.GMKeyEventBack, Consants.GMKeyEventBack);
        mKeyMap.put(GMKeyCommand.GMKeyEventFocusLeft_down, Consants.COM_FOCUS_ADD_DOWN);
        mKeyMap.put(GMKeyCommand.GMKeyEventFocusLeft_up, Consants.COM_FOCUS_ADD_UP);
        mKeyMap.put(GMKeyCommand.GMKeyEventFocusRight_down, Consants.COM_FOCUS_REDUCE_DOWN);
        mKeyMap.put(GMKeyCommand.GMKeyEventFocusRight_up, Consants.COM_FOCUS_REDUCE_UP);
        mKeyMap.put(GMKeyCommand.GMKeyEventHome, Consants.GMKeyEventHome);
        mKeyMap.put(GMKeyCommand.GMKeyEventMenu, Consants.GMKeyEventMenu);
        mKeyMap.put(GMKeyCommand.GMKeyEventVolumeDown, Consants.GMKeyEventVolumeDown);
        mKeyMap.put(GMKeyCommand.GMKeyEventVolumeUp, Consants.GMKeyEventVolumeUp);
        mKeyMap.put(GMKeyCommand.GMKeyEventFocusTurnOFF, Consants.GMKeyEventTurnOff);
        mKeyMap.put(GMKeyCommand.GMKeyEventFocusThreeD, Consants.GMKeyEventThreeD);
        mKeyMap.put(GMKeyCommand.GMKeyEventFunction, Consants.GMKeyEventFunction);
        mKeyMap.put(GMKeyCommand.GMkeyOpen, Consants.GMKeyOpen);
        mKeyMap.put(GMKeyCommand.GMKeyGamel, Consants.GMkeyEVentl);
        mKeyMap.put(GMKeyCommand.GMKeyGamer, Consants.GMkeyEVentr);
        mKeyMap.put(GMKeyCommand.GMKeyGamea, Consants.GMkeyEVenta);
        mKeyMap.put(GMKeyCommand.GMKeyGameb, Consants.GMkeyEVentb);
        mKeyMap.put(GMKeyCommand.GMKeyGamex, Consants.GMkeyEVentx);
        mKeyMap.put(GMKeyCommand.GMKeyGameY, Consants.GMkeyEVentY);
        mKeyMap.put(GMKeyCommand.GMKeyGameSelecter, Consants.GMkeyEVentselector);
        mKeyMap.put(GMKeyCommand.GMKeyGameleft, Consants.GMkeyEVentLeft);
        mKeyMap.put(GMKeyCommand.GMKeyGameright, Consants.GMkeyEVentRight);
        mKeyMap.put(GMKeyCommand.GMKeyGameup, Consants.GMkeyEVentUp);
        mKeyMap.put(GMKeyCommand.GMKeyGamedown, Consants.GMkeyEVentDown);

        mKeyMap.put(GMKeyCommand.GMKeyLeftDown, Consants.GMKeyStatusLeft);
        mKeyMap.put(GMKeyCommand.GMKeyLeftup, Consants.GMKeyPressLeft);

        mKeyMap.put(GMKeyCommand.GMKeyrightdonw, Consants.GMKeyStatusRight);
        mKeyMap.put(GMKeyCommand.GMKeyrightup, Consants.GMKeyPressRight);

        mKeyMap.put(GMKeyCommand.GMKeyupdonw, Consants.GMKeyStatusup);
        mKeyMap.put(GMKeyCommand.GMKeyupUp, Consants.GMKeyPressup);

        mKeyMap.put(GMKeyCommand.GMKeyDowndonw, Consants.GMKeyStatusdown);
        mKeyMap.put(GMKeyCommand.GMKeyDownup, Consants.GMKeyPressdown);

        mKeyMap.put(GMKeyCommand.GMKeyOkDown, Consants.GMKeyOkDown);
        mKeyMap.put(GMKeyCommand.GMKeyOkUp, Consants.GMKeyOkUp);
        mKeyMap.put(GMKeyCommand.GMKeyHomeDown, Consants.GMKeyEventHomeDOWN);
        mKeyMap.put(GMKeyCommand.GMKeyHomeUp, Consants.GMKeyEventHomeUP);
    }

    public static String createKeyCommand(GMKeyCommand command) {
        if (mKeyMap.containsKey(command.getKey())) {
            return mKeyMap.get(command.getKey());
        } else {
            return null;
        }
    }
}
