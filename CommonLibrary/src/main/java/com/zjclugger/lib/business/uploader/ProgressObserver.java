package com.zjclugger.lib.business.uploader;

/**
 * 自定义文件上传、下载观察者 (增强 RequestBaseObserver)
 */
public abstract class ProgressObserver<T> extends RequestBaseObserver<T> {

    public ProgressObserver() {
    }

    @Override
    public void onNext(T t) {
        if (t instanceof UploadFile) {
            onProgress((UploadFile) t);
        } else {
            super.onNext(t);
        }
    }
}
