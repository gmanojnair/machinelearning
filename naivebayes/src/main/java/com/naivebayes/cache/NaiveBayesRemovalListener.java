package com.naivebayes.cache;

import org.springframework.stereotype.Component;

import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

@Component
public class NaiveBayesRemovalListener implements RemovalListener{
	 
		
		public void onRemoval(RemovalNotification notification) {
			System.out.println("NaiveBayes associated with the key("+
					notification.getKey()+ ") is removed.");
		}
	 

}
