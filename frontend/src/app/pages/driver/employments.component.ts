import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DriverDataService } from './driver-data.service';
import { FieldDef, RecordsListComponent, RECORDS_TEMPLATE } from './records-list.component';

@Component({
  selector: 'app-employments',
  imports: [FormsModule],
  template: RECORDS_TEMPLATE
})
export class EmploymentsComponent extends RecordsListComponent<any> {
  override data = inject(DriverDataService);
  title = 'Employment History';
  singular = 'employer record';
  columns = ['Company','Position','Phone','From','To'];
  override fields: FieldDef[] = [{ name:'name',label:'Company Name',required:true },{ name:'position',label:'Position' },{ name:'phone',label:'Phone' },{ name:'fromDate',label:'From (YYYY-MM-DD)',required:true },{ name:'toDate',label:'To (YYYY-MM-DD)' }];

  loadRows() { return this.data.employments(); }
  addRow(form: any) { return this.data.addEmployment(form); }
  removeRow(row: any) { return this.data.deleteEmployment(row.uuid); }
}