import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes} from '@angular/router';
import {MainComponent} from 'app/dashboard/main/main/main.component';
import {NewComplaintComponent} from 'app/dashboard/complaint/new-complaint/new-complaint.component';
import {ComplaintsComponent} from 'app/dashboard/complaint/complaints/complaints.component';
import {AdminManagementDetailComponent} from 'app/admin/admin-management/admin-management-detail.component';
import {AdminManagementUpdateComponent} from 'app/admin/admin-management/admin-management-update.component';
import {Injectable} from '@angular/core';
import {ComplaintService} from 'app/dashboard/complaint/complaint.service';
import {ViewComplaintComponent} from 'app/dashboard/complaint/view-complaint/view-complaint.component';

@Injectable({ providedIn: 'root' })
export class ComplaintResolve implements Resolve<any> {
  constructor(private service: ComplaintService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    const id = route.params['complaint'] ? route.params['complaint'] : null;
    if (id) {
      return this.service.find(id);
    }
    return {};
  }
}

export const complaintRoute: Routes = [
  {
    path: 'newComplaint',
    component: NewComplaintComponent,
    resolve: {
      complaint: ComplaintResolve
    }
  },
  {
    path: ':complaint/view',
    component: ViewComplaintComponent,
    resolve: {
      complaint: ComplaintResolve
    }
  },
  {
    path: ':complaint/edit',
    component: NewComplaintComponent,
    resolve: {
      complaint: ComplaintResolve
    }
  },
  {
    path: 'complaints',
    component: ComplaintsComponent
  }
];
