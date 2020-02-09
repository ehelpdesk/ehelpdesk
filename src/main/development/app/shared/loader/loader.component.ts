import {Component, OnDestroy, OnInit} from '@angular/core';
import {EventManager} from 'app/shared';
import {Subscription} from 'rxjs';

@Component({
  selector: 'eh-loader',
  templateUrl: './loader.component.html',
  styles: []
})
export class LoaderComponent implements OnInit, OnDestroy {
  showLoader: boolean;
  private showLoaderSubscription: Subscription;

  constructor(private eventManager: EventManager) {
  }

  ngOnInit() {
    this.showLoaderSubscription = this.eventManager.subscribe('showLoader', event => {
      this.showLoader = event.content;
    });
  }

  ngOnDestroy(): void {
    if (this.showLoaderSubscription) {
      this.eventManager.destroy(this.showLoaderSubscription);
    }
  }

}
