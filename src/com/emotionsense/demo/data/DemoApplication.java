package com.emotionsense.demo.data;

import android.app.Application;

public class DemoApplication extends Application
{
	private static DemoApplication instance;
	
	public DemoApplication()
	{
		super();
		instance = this;
	}
	
	public static Application getContext()
	{
		return instance;
	}
}
