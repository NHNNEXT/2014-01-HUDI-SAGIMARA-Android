package org.nhnnext.sagimara.noperation;

import java.util.HashMap;

public class OperationMapping {
	static public HashMap<String, NOperation> map = new HashMap<String, NOperation>()  {
		private static final long serialVersionUID = -3286315173722334990L;
	{
		put("getUserInfo", new GetUserInfoOperation("/test"));
		put("requestVerification", new RequestVerificationOperation("/insert/RequestData"));
		put("getRequestAboutMe", new GetRequestsAboutMeOperation("/push/alarm"));
		put("insertPhoto", new InsertPhohoForVerificationOperation("/insert/photoData"));
		put("insertLocation", new InsertLocationForVerificationOperation("/insert/locationData"));
	}};
	
	public NOperation getNOperation(String operationName) {
		return map.get(operationName);
	}
	
	public boolean isContain(String path) {
		return map.containsKey(path);	
	}
}
