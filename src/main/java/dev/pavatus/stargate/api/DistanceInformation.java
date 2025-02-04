package dev.pavatus.stargate.api;

public record DistanceInformation(double distance, boolean dimChange, boolean rotChange) {
	public DistanceInformation {
		if (distance < 0) {
			throw new IllegalArgumentException("Distance cannot be negative");
		}
	}
}
