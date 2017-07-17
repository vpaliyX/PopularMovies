package com.popularmovies.vpaliy.popularmoviesapp.ui.actor;

import android.support.annotation.NonNull;
import android.util.Log;

import com.popularmovies.vpaliy.data.utils.scheduler.BaseSchedulerProvider;
import com.popularmovies.vpaliy.domain.model.ActorDetails;
import com.popularmovies.vpaliy.domain.repository.IDetailsRepository;
import com.popularmovies.vpaliy.popularmoviesapp.di.scope.ViewScope;

import java.util.List;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.popularmovies.vpaliy.popularmoviesapp.ui.actor.ActorContract.View;

@ViewScope
public class ActorPresenter implements ActorContract.Presenter{

    private View view;
    private IDetailsRepository<ActorDetails> detailsRepository;
    private BaseSchedulerProvider schedulerProvider;

    @Inject
    public ActorPresenter(@NonNull IDetailsRepository<ActorDetails> detailsRepository,
                          @NonNull BaseSchedulerProvider schedulerProvider){
        this.detailsRepository=detailsRepository;
        this.schedulerProvider=schedulerProvider;
    }

    @Override
    public void attachView(@NonNull View view) {
        this.view=checkNotNull(view);
    }

    @Override
    public void start(int id) {
        detailsRepository.get(id)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::processData,this::catchError);
    }

    private void processData(ActorDetails details){
        List<String> images=details.getTaggedImages();
        view.showBackground(images.get(0));

    }

    private void catchError(Throwable error){
        error.printStackTrace();
    }

    @Override
    public void stop() {

    }
}
