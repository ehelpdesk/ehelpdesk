import {NgModule} from '@angular/core';
import {SharedModule} from 'app/shared/shared.module';
import {RouterModule} from '@angular/router';
import {complaintRoute} from 'app/dashboard/complaint/complaint.route';
import { NewComplaintComponent } from './new-complaint/new-complaint.component';
import { ComplaintsComponent } from './complaints/complaints.component';
import { DeleteComplaintModalComponent } from './delete-complaint-modal/delete-complaint-modal.component';
import { ViewComplaintComponent } from './view-complaint/view-complaint.component';

@NgModule({
  declarations: [NewComplaintComponent, ComplaintsComponent, DeleteComplaintModalComponent, ViewComplaintComponent],
  entryComponents: [DeleteComplaintModalComponent],
  imports: [SharedModule, RouterModule.forChild(complaintRoute)],
})
export class ComplaintModule { }
