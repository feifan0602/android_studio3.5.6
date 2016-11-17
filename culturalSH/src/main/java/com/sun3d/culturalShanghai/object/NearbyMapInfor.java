package com.sun3d.culturalShanghai.object;

import java.io.Serializable;
import java.util.List;

public class NearbyMapInfor implements Serializable {
	private List<EventInfo> EventlistItem;
	private List<VenueDetailInfor> VenuelistItem;

	public List<EventInfo> getEventlistItem() {
		return EventlistItem;
	}

	public void setEventlistItem(List<EventInfo> eventlistItem) {
		EventlistItem = eventlistItem;
	}

	public List<VenueDetailInfor> getVenuelistItem() {
		return VenuelistItem;
	}

	public void setVenuelistItem(List<VenueDetailInfor> venuelistItem) {
		VenuelistItem = venuelistItem;
	}

}
