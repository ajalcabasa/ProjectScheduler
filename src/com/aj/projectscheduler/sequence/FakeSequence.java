package com.aj.projectscheduler.sequence;

public class FakeSequence {

	protected static Long SEQUENCE = 0l;
	
	public static Long getNext() {
		return SEQUENCE++;
	}
	
}
