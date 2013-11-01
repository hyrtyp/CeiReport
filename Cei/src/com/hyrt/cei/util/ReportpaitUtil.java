package com.hyrt.cei.util;

import java.util.ArrayList;
import java.util.List;
import com.hyrt.cei.vo.ReportpaitElement;

public class ReportpaitUtil {
	public static boolean getChild(List<ReportpaitElement> all,
			String parntid) {
		for (ReportpaitElement reportpaitElement : all) {
			if (reportpaitElement.getParent().equals(parntid)) {
				return true;
			}
		}
		return false;
	}

}
