import { Component, OnDestroy, ChangeDetectorRef  } from '@angular/core';
import {EventManager} from 'app/shared/service/event-manager.service';

@Component({
  selector: 'qf-loading-screen',
  templateUrl: './loadingScreen.component.html',
  styles: []
})

export class LoadingScreenComponent implements OnDestroy {
  private loading = false;
  loadingMessage: any;
  subscription: any;

  constructor(private eventManager: EventManager, private changeDetectorRef: ChangeDetectorRef) {
    this.subscription = this.eventManager.subscribe('loadingComponent', event => {
        this.loading = event.content.isLoading;
        this.loadingMessage = event.content.loadingMessage;
        this.changeDetectorRef.detectChanges();
    });
  }

  ngOnDestroy(): void {
      this.eventManager.destroy(this.subscription);
  }
}
