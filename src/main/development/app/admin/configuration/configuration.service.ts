import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';

@Injectable({ providedIn: 'root' })
export class ConfigurationService {
    constructor(private http: HttpClient) {}

    get(): Observable<any> {
        return this.http.get(SERVER_API_URL + 'management/configprops', { observe: 'response' }).pipe(
            map((res: HttpResponse<any>) => {
                const properties: any[] = [];
                const propertiesObject = this.getConfigPropertiesObjects(res.body);
                for (const key in propertiesObject) {
                    if (Object.prototype.hasOwnProperty.call(propertiesObject, key)) {
                        properties.push(propertiesObject[key]);
                    }
                }

                return properties.sort((propertyA, propertyB) => {
                    return propertyA.prefix === propertyB.prefix ? 0 : propertyA.prefix < propertyB.prefix ? -1 : 1;
                });
            })
        );
    }

    getConfigPropertiesObjects(res: Record<string, any>) {
        if (res['contexts'] !== undefined) {
            for (const key in res['contexts']) {
                if (!key.startsWith('bootstrap')) {
                    return res['contexts'][key]['beans'];
                }
            }
        }
        return res['contexts']['Queryfyer']['beans'];
    }

    getEnv(): Observable<any> {
        return this.http.get(SERVER_API_URL + 'management/env', { observe: 'response' }).pipe(
            map((res: HttpResponse<any>) => {
                const properties: any = {};
                const propertySources = res.body['propertySources'];

                for (const propertyObject of propertySources) {
                    const name = propertyObject['name'];
                    const detailProperties = propertyObject['properties'];
                    const vals: any[] = [];
                    for (const keyDetail in detailProperties) {
                        if (Object.prototype.hasOwnProperty.call(detailProperties, keyDetail)) {
                            vals.push({ key: keyDetail, val: detailProperties[keyDetail]['value'] });
                        }
                    }
                    properties[name] = vals;
                }
                return properties;
            })
        );
    }
}
