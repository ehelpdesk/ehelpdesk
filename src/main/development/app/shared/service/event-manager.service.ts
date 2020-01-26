import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Observer } from 'rxjs';
import { Subscription } from 'rxjs';
import { filter } from 'rxjs/operators';
import { share } from 'rxjs/operators';

@Injectable({ providedIn: 'root' })
export class EventManager {
    observable: Observable<any>;
    observer: Observer<any>;

    constructor() {
      console.log('event manager constructor');
      this.observable = new Observable((observer: Observer<any>) => {
        console.log('Observer initiated');
        this.observer = observer;
      }).pipe(share());
    }

    /**
     * Method to broadcast the event to observer
     */
    broadcast(event) {
        if (this.observer != null) {
            this.observer.next(event);
        }
    }

    /**
     * Method to subscribe to an event with callback
     */
    subscribe(eventName, callback) {
        const subscriber: Subscription = this.observable
            .pipe(
                filter(event => {
                    return event.name === eventName;
                })
            )
            .subscribe(callback);
        return subscriber;
    }

    /**
     * Method to unsubscribe the subscription
     */
    destroy(subscriber: Subscription) {
        subscriber.unsubscribe();
    }
}
