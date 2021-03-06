package com.liferay.mobile.screens.webcontent.list;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import com.liferay.mobile.screens.R;
import com.liferay.mobile.screens.base.list.BaseListScreenlet;
import com.liferay.mobile.screens.cache.OfflinePolicy;
import com.liferay.mobile.screens.context.LiferayServerContext;
import com.liferay.mobile.screens.webcontent.WebContent;
import com.liferay.mobile.screens.webcontent.list.interactor.WebContentListInteractor;
import com.liferay.mobile.screens.webcontent.list.interactor.WebContentListInteractorImpl;
import com.liferay.mobile.screens.webcontent.list.interactor.WebContentListInteractorListener;

import java.util.Locale;

/**
 * @author Javier Gamarra
 */
public class WebContentListScreenlet extends BaseListScreenlet<WebContent, WebContentListInteractor>
	implements WebContentListInteractorListener {

	public WebContentListScreenlet(Context context) {
		super(context);
	}

	public WebContentListScreenlet(Context context, AttributeSet attributes) {
		super(context, attributes);
	}

	public WebContentListScreenlet(Context context, AttributeSet attributes, int defaultStyle) {
		super(context, attributes, defaultStyle);
	}

	@Override
	public void loadingFromCache(boolean success) {
		if (_listener != null) {
			_listener.loadingFromCache(success);
		}
	}

	@Override
	public void retrievingOnline(boolean triedInCache, Exception e) {
		if (_listener != null) {
			_listener.retrievingOnline(triedInCache, e);
		}
	}

	@Override
	public void storingToCache(Object object) {
		if (_listener != null) {
			_listener.storingToCache(object);
		}
	}

	public OfflinePolicy getOfflinePolicy() {
		return _offlinePolicy;
	}

	public void setOfflinePolicy(OfflinePolicy offlinePolicy) {
		_offlinePolicy = offlinePolicy;
	}

	public long getGroupId() {
		return _groupId;
	}

	public void setGroupId(long groupId) {
		_groupId = groupId;
	}

	public long getFolderId() {
		return _folderId;
	}

	public void setFolderId(long folderId) {
		_folderId = folderId;
	}

	@Override
	public WebContentListListener getListener() {
		return _listener;
	}

	public void setListener(WebContentListListener listener) {
		_listener = listener;
	}

	@Override
	protected void loadRows(WebContentListInteractor interactor, int startRow, int endRow, Locale locale) throws Exception {
		interactor.load(_groupId, _folderId, startRow, endRow, locale);
	}

	@Override
	protected WebContentListInteractor createInteractor(String actionName) {
		return new WebContentListInteractorImpl(getScreenletId(), _offlinePolicy);
	}

	@Override
	protected View createScreenletView(Context context, AttributeSet attributes) {
		TypedArray typedArray = context.getTheme().obtainStyledAttributes(
			attributes, R.styleable.WebContentListScreenlet, 0, 0);

		Integer offlinePolicy = typedArray.getInteger(
			R.styleable.WebContentListScreenlet_offlinePolicy,
			OfflinePolicy.REMOTE_ONLY.ordinal());
		_offlinePolicy = OfflinePolicy.values()[offlinePolicy];

		_folderId = castToLongOrUseDefault(typedArray.getString(
			R.styleable.WebContentListScreenlet_folderId), 0);

		long groupId = LiferayServerContext.getGroupId();

		_groupId = castToLongOrUseDefault(typedArray.getString(
			R.styleable.WebContentListScreenlet_groupId), groupId);

		typedArray.recycle();

		return super.createScreenletView(context, attributes);
	}

	private OfflinePolicy _offlinePolicy;
	private long _groupId;
	private long _folderId;
	private WebContentListListener _listener;
}
