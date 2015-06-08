package com.emotionsense.demo.data.loggers;

import java.util.HashMap;

import android.content.Context;

import com.emotionsense.demo.data.DemoApplication;
import com.ubhave.datahandler.config.DataStorageConfig;
import com.ubhave.datahandler.except.DataHandlerException;
import com.ubhave.datahandler.loggertypes.AbstractAsyncTransferLogger;
import com.ubhave.datahandler.loggertypes.AbstractDataLogger;
import com.ubhave.sensormanager.ESException;

public class AsyncUnencryptedFiles extends AbstractAsyncTransferLogger
{
	private static AsyncUnencryptedFiles instance;

	public static AbstractDataLogger getInstance() throws ESException, DataHandlerException
	{
		if (instance == null)
		{
			instance = new AsyncUnencryptedFiles(DemoApplication.getContext());
		}
		return instance;
	}

	protected AsyncUnencryptedFiles(final Context context) throws DataHandlerException, ESException
	{
		super(context, DataStorageConfig.STORAGE_TYPE_FILES);
	}

	@Override
	protected String getDataPostURL()
	{
		return RemoteServerDetails.FILE_POST_URL;
	}
	
	@Override
	protected String getPostKey()
	{
		return RemoteServerDetails.DATA_KEY;
	}
	
	@Override
	protected String getSuccessfulPostResponse()
	{
		return RemoteServerDetails.RESPONSE_ON_SUCCESS;
	}
	
	@Override
	protected HashMap<String, String> getPostParameters()
	{
		// Note: no additional parameters used in demo.
		return null;
	}

	@Override
	protected long getDataLifeMillis()
	{
		// Note: all data that is more than 1 hour old will be transferred
		return 1000L * 60 * 60 * 1;
	}

	@Override
	protected long getTransferAlarmLengthMillis()
	{
		// Note: transfer alarm will fire every 2 hours
		return 1000L * 60 * 60 * 2;
	}

	@Override
	protected String getStorageName()
	{
		return "Demo-Unencrypted-Async-Storage";
	}

	@Override
	protected String getUniqueUserId()
	{
		// Note: this should not be a static string
		return "test-user-id";
	}

	@Override
	protected String getDeviceId()
	{
		// Note: this should not be a static string
		return "test-device-id";
	}

	@Override
	protected boolean shouldPrintLogMessages()
	{
		// Note: return false to turn off Log.d messages
		return true;
	}

	@Override
	protected String getEncryptionPassword()
	{
		// Note: return non-null password to encrypt data
		return null;
	}
}
