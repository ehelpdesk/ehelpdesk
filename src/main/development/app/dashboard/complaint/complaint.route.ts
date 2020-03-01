import {Routes} from '@angular/router';
import {MainComponent} from 'app/dashboard/main/main/main.component';
import {NewComplaintComponent} from 'app/dashboard/complaint/new-complaint/new-complaint.component';
import {ComplaintsComponent} from 'app/dashboard/complaint/complaints/complaints.component';

export const complaintRoute: Routes = [
  {
    path: 'newComplaint',
    component: NewComplaintComponent
  },
  {
    path: 'complaints',
    component: ComplaintsComponent
  }
];
