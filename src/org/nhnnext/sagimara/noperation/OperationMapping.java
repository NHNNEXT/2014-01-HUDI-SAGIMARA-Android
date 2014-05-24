package org.nhnnext.sagimara.noperation;

import java.util.HashMap;

public class OperationMapping {
	static public HashMap<String, NOperation> map = new HashMap<String, NOperation>()  {
		private static final long serialVersionUID = -3286315173722334990L;
	{
		put("getUserInfo", new GetUserInfoOperation("/test"));
	}};
	
	public NOperation getNOperation(String operationName) {
		return map.get(operationName);
	}
	
	public boolean isContain(String path) {
		return map.containsKey(path);	
	}
}
