package com.freeApp.kknni.common;

import javax.annotation.PostConstruct;

public abstract class DTDJob extends Job {

	@PostConstruct
	public abstract void setJob();

	public abstract void makeJob();

	@Override
	public void extract() {
		super.extract();
	}

	@Override
	public void transform() {
		super.transform();
	}

	@Override
	public void load() {
		super.load();
	}
}
