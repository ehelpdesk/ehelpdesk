import { Component, OnInit } from '@angular/core';

import { ConfigurationService } from './configuration.service';

@Component({
    selector: 'qf-configuration',
    templateUrl: './configuration.component.html'
})
export class ConfigurationComponent implements OnInit {
    allConfiguration: any = null;
    configuration: any = null;
    configKeys: any[];
    filter: string;
    orderProp: string;
    reverse: boolean;

    constructor(private configurationService: ConfigurationService) {
        this.configKeys = [];
        this.filter = '';
        this.orderProp = 'prefix';
        this.reverse = false;
    }

    keys(dict): string[] {
        return dict === undefined ? [] : Object.keys(dict);
    }

    ngOnInit() {
        this.configurationService.get().subscribe(configuration => {
            this.configuration = configuration;

            for (const config of configuration) {
                if (config.properties !== undefined) {
                    this.configKeys.push(Object.keys(config.properties));
                }
            }
        });

        this.configurationService.getEnv().subscribe(configuration => {
            this.allConfiguration = configuration;
        });
    }
}
