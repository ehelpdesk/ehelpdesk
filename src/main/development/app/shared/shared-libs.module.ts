import { NgModule } from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

@NgModule({
    imports: [NgbModule],
    exports: [FormsModule, HttpClientModule, CommonModule, NgbModule, ReactiveFormsModule]
})
export class SharedLibsModule {}
