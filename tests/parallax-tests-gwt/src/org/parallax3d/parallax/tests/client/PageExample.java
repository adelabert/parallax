/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file is part of Parallax project.
 * 
 * Parallax is free software: you can redistribute it and/or modify it 
 * under the terms of the Creative Commons Attribution 3.0 Unported License.
 * 
 * Parallax is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the Creative Commons Attribution 
 * 3.0 Unported License. for more details.
 * 
 * You should have received a copy of the the Creative Commons Attribution 
 * 3.0 Unported License along with Parallax. 
 * If not, see http://creativecommons.org/licenses/by/3.0/.
 */

package org.parallax3d.parallax.tests.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.parallax3d.parallax.App;
import org.parallax3d.parallax.Rendering;
import org.parallax3d.parallax.events.AnimationReadyListener;
import org.parallax3d.parallax.graphics.renderers.Plugin;
import org.parallax3d.parallax.tests.TestAnimation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import org.parallax3d.parallax.platforms.gwt.GwtApp;

/**
 * A widget used to show Parallax examples.
 */
public abstract class PageExample extends SimpleLayoutPanel
	implements AnimationReadyListener
{

	/**
	 * Generic callback used for asynchronously loaded data.
	 * 
	 * @param <T> the data type
	 */
	public static interface Callback<T>
	{
		void onError();
		void onSuccess(T value);
	}

//	/**
//	 * {@link RenderingPanel} where example will be shown
//	 */
//	protected RenderingPanel renderingPanel;
	
	private AlertLoading loadingPanel;
	private boolean isSceneHasObjects = false;
	/**
	 * A description of an example.
	 */
	private final HTML description;

	/**
	 * The name of the example.
	 */
	private final String name;

	/**
	 * The source code associated with an example.
	 */
	private String sourceCode;

	/**
	 * The view that holds the name, description, and example.
	 */
	private PanelButtons view;

	/**
	 * Whether the tests widget has been initialized.
	 */
	private boolean widgetInitialized;

	/**
	 * Whether the tests widget is (asynchronously) initializing.
	 */
	private boolean widgetInitializing;
	
	private Plugin effectPlugin;

	/**
	 * Default constructor should be called in an example (daughter) class
	 * 
	 * @param name
	 *            a text name of an example
	 * @param description
	 *            a text description of an example
	 */
	public PageExample(String name, String description)
	{
		this.name = name;
		this.description = new HTML(description);
	}

	protected abstract void asyncOnInitialize(final AsyncCallback<TestAnimation> callback);

	public void onAnimationReady()
	{
		final Rendering rendering = App.app.getRendering();

		view.setDebugger(rendering.getRenderer());
//
//		this.renderingPanel.setAnimationUpdateHandler(new RenderingPanel.AnimationUpdateHandler() {
//
//			@Override
//			public void onUpdate(double duration) {
//				view.getDebugger().update();
//			}
//		});

    	view.switchAnimation.setEnabled(true);
    	view.switchAnimation.setDown(true);
    	view.switchAnimation.addClickHandler(new ClickHandler() {
    		public void onClick(ClickEvent event) {
    			if (view.switchAnimation.isDown())
					rendering.resume();
    			else
					rendering.pause();
    		}
    	});

    	view.switchFullScreen.setEnabled(rendering.supportsDisplayModeChange());
    	view.switchFullScreen.addClickHandler(new ClickHandler() {
    		public void onClick(ClickEvent event) {
    			if (view.switchFullScreen.isDown()) {
//    				ContentWidget.this.renderingPanel.toFullScreen();
    				view.switchFullScreen.setDown(false);
    			}
    		}
    	});

    	view.setEnableEffectSwitch(this.isEnabledEffectSwitch());

    	view.switchEffectAnaglyph.addClickHandler(new ClickHandler() {
    		public void onClick(ClickEvent event) {
				rendering.getRenderer().deletePlugin(PageExample.this.effectPlugin);
//    			if (view.switchEffectAnaglyph.isDown())
//					ContentWidget.this.effectPlugin = new Anaglyph(
//							ContentWidget.this.renderingPanel.getRenderer(),
//							ContentWidget.this.renderingPanel.getAnimatedScene().getScene());
    		}
    	});

    	view.switchEffectStereo.addClickHandler(new ClickHandler() {
    		public void onClick(ClickEvent event) {
				rendering.getRenderer().deletePlugin(PageExample.this.effectPlugin);
//				if (view.switchEffectStereo.isDown())
//					ContentWidget.this.effectPlugin = new Stereo(
//							ContentWidget.this.renderingPanel.getRenderer(),
//							ContentWidget.this.renderingPanel.getAnimatedScene().getScene());
    		}
    	});

    	view.switchEffectParallaxBarrier.addClickHandler(new ClickHandler() {
    		public void onClick(ClickEvent event) {
				rendering.getRenderer().deletePlugin(PageExample.this.effectPlugin);
//				if (view.switchEffectParallaxBarrier.isDown())
//					ContentWidget.this.effectPlugin = new ParallaxBarrier(
//							ContentWidget.this.renderingPanel.getRenderer(),
//							ContentWidget.this.renderingPanel.getAnimatedScene().getScene());
    		}
    	});

    	view.switchEffectOculusRift.addClickHandler(new ClickHandler() {
    		public void onClick(ClickEvent event) {
				rendering.getRenderer().deletePlugin(PageExample.this.effectPlugin);
//				if (view.switchEffectOculusRift.isDown())
//					ContentWidget.this.effectPlugin = new OculusRift(
//							ContentWidget.this.renderingPanel.getRenderer(),
//							ContentWidget.this.renderingPanel.getAnimatedScene().getScene());
    		}
    	});

    	view.switchEffectNone.addClickHandler(new ClickHandler() {
    		public void onClick(ClickEvent event) {
				rendering.getRenderer().deletePlugin(PageExample.this.effectPlugin);
    		}
    	});

	}

//
//	@Override
//	public void onContextError(Context3dErrorEvent event)
//	{
//		if(this.loadingPanel != null && this.loadingPanel.isVisible())
//		{
//			this.loadingPanel.hide();
//		}
//
//		this.renderingPanel.add(new BadCanvasPanel(event.getMessage()));
//	}

	/**
	 * Called when an example attached with parent Widget.
	 */
	@Override
	protected void onLoad()
	{
		if (view == null) 
		{		
			view = new PanelButtons();
//			view.setName(getName());
//			view.setDescription(getDescription());
			setWidget(view);
		}

		ensureWidgetInitialized();
		super.onLoad();
	}
	
	@Override
	protected void onUnload() 
	{
		view = null;
//		renderingPanel = null;
		widgetInitializing = widgetInitialized = false;
		super.onUnload();
	}
	
	/**
	 * Ensure that an example has been initialized. Note that
	 * initialization can fail if there is a network failure.
	 */
	private void ensureWidgetInitialized()
	{
		if (widgetInitializing || widgetInitialized)
			return;

		widgetInitializing = true;

		asyncOnInitialize(new AsyncCallback<TestAnimation>() {
			public void onFailure(Throwable reason)
			{
				widgetInitializing = false;
				Window.alert("Failed to download code for this widget (" + reason + ")");
			}

			public void onSuccess(TestAnimation demoAnimatedScene)
			{
				widgetInitializing = false;
				widgetInitialized = true;

				// Finally setup RenderingPanel attached to the loaded example
		        if (demoAnimatedScene != null)
		        {

					((GwtApp)App.app).setRendering(view.getRenderingPanel(), ((GwtApp) App.app).getConfig());
					App.app.getRendering().setAnimation(demoAnimatedScene);

//		    		renderingPanel.addSceneLoadingHandler(ContentWidget.this);
//		    		renderingPanel.addCanvas3dErrorHandler(ContentWidget.this);
					App.app.getRendering().setAnimationReadyListener(PageExample.this);
		        }
			}
		});
	}
	
	protected boolean isEnabledEffectSwitch() {
		return true;
	}

//	protected void loadRenderingPanelAttributes(RenderingPanel renderingPanel)
//	{
//		/* Empty */
//	}

	/**
	 * Send a request for source code.
	 * 
	 * @param callback
	 *            the {@link RequestCallback} to send
	 * @param url
	 *            the URL to target
	 */
	private void sendSourceRequest(RequestCallback callback, String url)
	{
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, GWT.getModuleBaseURL() + url);
		builder.setCallback(callback);
		try 
		{
			builder.send();
		} 
		catch (RequestException e) 
		{
			callback.onError(null, e);
		}
	}
}