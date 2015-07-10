package com.emotionsense.demo.data.loggers;

import android.content.Context;

import com.emotionsense.demo.data.DemoApplication;
import com.ubhave.datahandler.config.DataStorageConfig;
import com.ubhave.datahandler.except.DataHandlerException;
import com.ubhave.datahandler.loggertypes.AbstractDataLogger;
import com.ubhave.datahandler.loggertypes.AbstractStoreOnlyLogger;
import com.ubhave.sensormanager.ESException;

public class StoreOnlyUnencryptedFiles extends AbstractStoreOnlyLogger
{
	private static StoreOnlyUnencryptedFiles instance;
	
	public static AbstractDataLogger getInstance() throws ESException, DataHandlerException
	{
		if (instance == null)
		{
			instance = new StoreOnlyUnencryptedFiles(DemoApplication.getContext());
		}
		return instance;
	}

	protected StoreOnlyUnencryptedFiles(Context context) throws DataHandlerException, ESException
	{
		super(context, DataStorageConfig.STORAGE_TYPE_FILES);
	}

	@Override
	protected String getFileStorageName()
	{
		return "Demo-Unencrypted-Store-Only-Storage";
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
