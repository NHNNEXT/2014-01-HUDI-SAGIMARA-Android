package org.nhnnext.sagimara.noperation;

import org.json.JSONObject;

import android.content.Context;
import android.view.View;

public interface NOperation {

	abstract JSONObject run(String...params);
	abstract void doPostRun(JSONObject jsonResult, Context context, View view);

}
