import { Component, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import {Alert, AlertService, AlertType} from 'app/shared/service/alert.service';
import { EventManager } from 'app/shared/service/event-manager.service';

@Component({
    selector: 'eh-alert-error',
    template: `
        <div class="alerts" role="alert">
            <div *ngFor="let alert of alerts" [ngClass]="setClasses(alert)">
                <ngb-alert *ngIf="alert && alert.type && alert.msg" [type]="alert.type" (close)="alert.close(alerts)">
                    <pre [innerHTML]="alert.msg"></pre>
                </ngb-alert>
            </div>
        </div>
    `
})
export class AlertErrorComponent implements OnDestroy {
    alerts: any[];
    cleanHttpErrorListener: Subscription;
    cleanHttpSuccessListener: Subscription;
    constructor(private alertService: AlertService, private eventManager: EventManager) {
        this.alerts = [];

        this.cleanHttpSuccessListener = eventManager.subscribe('Ehelpdesk_httpSuccess', response => {
            const successAlert: AlertType = 'success';
            this.addErrorAlert(response.content, null, null, successAlert);
        });

        this.cleanHttpErrorListener = eventManager.subscribe('Ehelpdesk_httpError', response => {
          let i;
            const httpErrorResponse = response.content;
            switch (httpErrorResponse.status) {
                // connection refused, server not reachable
                case 0:
                    this.addErrorAlert('Server not reachable', 'error.server.not.reachable');
                    break;

                case 400: {
                    const arr = httpErrorResponse.headers.keys();
                    let errorHeader = null;
                    let entityKey = null;
                    arr.forEach(entry => {
                        if (entry.toLowerCase().endsWith('app-error')) {
                            errorHeader = httpErrorResponse.headers.get(entry);
                        } else if (entry.toLowerCase().endsWith('app-params')) {
                            entityKey = httpErrorResponse.headers.get(entry);
                        }
                    });
                    if (errorHeader) {
                        const entityName = '';
                        this.addErrorAlert(errorHeader, errorHeader, {entityName}, 'danger');
                    } else if (httpErrorResponse.error !== '' && httpErrorResponse.error.fieldErrors) {
                        const fieldErrors = httpErrorResponse.error.fieldErrors;
                        for (i = 0; i < fieldErrors.length; i++) {
                            const fieldError = fieldErrors[i];
                            if (['Min', 'Max', 'DecimalMin', 'DecimalMax'].includes(fieldError.message)) {
                                fieldError.message = 'Size';
                            }
                            // convert 'something[14].other[4].id' to 'something[].other[].id' so translations can be written to it
                            const convertedField = fieldError.field.replace(/\[\d*\]/g, '[]');
                            const fieldName = '';
                            this.addErrorAlert('Error on field "' + fieldName + '"', 'error.' + fieldError.message, {fieldName}, 'danger');
                        }
                    } else if (httpErrorResponse.error !== '' && httpErrorResponse.error.message) {
                        this.addErrorAlert(httpErrorResponse.error.message, httpErrorResponse.error.message, httpErrorResponse.error.params, 'danger');
                    } else {
                        this.addErrorAlert(httpErrorResponse.error);
                    }
                    break;
                }
                case 404:
                    this.addErrorAlert('Not found', 'error.url.not.found');
                    break;

                default:
                    if (httpErrorResponse.error !== '' && httpErrorResponse.error.message) {
                        this.addErrorAlert(httpErrorResponse.error.message);
                    } else {
                        this.addErrorAlert(httpErrorResponse.error);
                    }
            }
        });
    }

    setClasses(alert) {
        return {
            'toast': alert.toast,
            [alert.position]: true
        };
    }

    ngOnDestroy() {
        if (this.cleanHttpErrorListener !== undefined && this.cleanHttpErrorListener !== null) {
            this.eventManager.destroy(this.cleanHttpErrorListener);
            this.alerts = [];
        }
        if (this.cleanHttpSuccessListener !== undefined && this.cleanHttpSuccessListener !== null) {
            this.eventManager.destroy(this.cleanHttpSuccessListener);
            this.alerts = [];
        }
    }

    addErrorAlert(message, key?, data?, type: AlertType = 'danger') {
        message = key && key !== null ? key : message;

        const newAlert: Alert = {
            type,
            msg: message,
            params: data,
            timeout: 50000,
            toast: this.alertService.isToast(),
            scoped: true
        };

        this.alerts.push(this.alertService.addAlert(newAlert, this.alerts));
    }
}
