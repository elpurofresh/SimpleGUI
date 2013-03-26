package pinger;

import javax.swing.event.EventListenerList;

public class EventSource {

	protected EventListenerList listenerList = new EventListenerList();
	
	public synchronized void addEventListener(EventClassListener listener){
		listenerList.add(EventClassListener.class, listener);
	}
	
	public synchronized void removeEventListener(EventClassListener listener){
		listenerList.remove(EventClassListener.class, listener);
	}
	
	@SuppressWarnings("unused")
	private synchronized void fireEvent(EventClass evt){
		//1. GET ALL THE LISTENERS
		Object[] listeners = listenerList.getListenerList();
		
		/*2. SEARCH THE APPROPRIATE LISTENER   
	    (= the one that implements MyEventListener)  
	    Each listener occupies two elements -   
	    the first is the listener class  
	    and the second is the listener instance!*/
		for (int i = 0; i < listeners.length; i = i +2) {
			if (listeners[i] == EventClassListener.class) {
				((EventClassListener) listeners[i+1]).eventOccurred(evt);
			}
		}
	}
}
