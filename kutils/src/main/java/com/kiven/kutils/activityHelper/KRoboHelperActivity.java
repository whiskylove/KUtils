package com.kiven.kutils.activityHelper;

import android.app.Activity;
import android.os.Bundle;

import com.google.inject.Inject;
import com.google.inject.Key;

import java.util.HashMap;
import java.util.Map;

import roboguice.RoboGuice;
import roboguice.activity.event.OnContentChangedEvent;
import roboguice.activity.event.OnStopEvent;
import roboguice.context.event.OnCreateEvent;
import roboguice.context.event.OnDestroyEvent;
import roboguice.event.EventManager;
import roboguice.inject.ContentViewListener;
import roboguice.inject.RoboInjector;
import roboguice.util.RoboContext;

/**
 * Created by kiven on 16/7/3.
 */
public class KRoboHelperActivity extends KHelperActivity implements RoboContext {
    protected EventManager eventManager;
    protected HashMap<Key<?>,Object> scopedObjects = new HashMap<Key<?>, Object>();

    /*
    TODO 要不配置RoboBlender
    https://github.com/roboguice/roboguice/wiki/RoboBlender-wiki

    TODO 要不就不使用RoboBlender
    static {
        RoboGuice.setUseAnnotationDatabases(false);//必须调用在使用第一个注解之前,否则会出问题
    }

    TODO 必须二选一, 建议使用RoboBlender
    */

    @Inject
    ContentViewListener ignored; // BUG find a better place to put this

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        KActivityHelper helper = getHelper(savedInstanceState);
        if (helper == null) {
            super.onCreate(savedInstanceState);
            return;
        }

        final RoboInjector injector = RoboGuice.getInjector(this);
        eventManager = injector.getInstance(EventManager.class);
        injector.injectMembersWithoutViews(helper);
        super.onCreate(savedInstanceState);
        eventManager.fire(new OnCreateEvent<Activity>(this,savedInstanceState));
    }

    /*protected Object getInjectorMember() {
        return this;
    }*/

    @Override
    protected void onStop() {
        try {
            eventManager.fire(new OnStopEvent(this));
        } finally {
            super.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        try {
            eventManager.fire(new OnDestroyEvent<Activity>(this));
        } finally {
            try {
                RoboGuice.destroyInjector(this);
            } finally {
                super.onDestroy();
            }
        }
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        RoboGuice.getInjector(this).injectViewMembers(this);
        eventManager.fire(new OnContentChangedEvent(this));
    }
    @Override
    public Map<Key<?>, Object> getScopedObjectMap() {
        return scopedObjects;
    }
}
