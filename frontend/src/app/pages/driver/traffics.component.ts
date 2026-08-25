import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DriverDataService } from './driver-data.service';
import { FieldDef, RecordsListComponent, RECORDS_TEMPLATE } from './records-list.component';

@Component({
  selector: 'app-traffics',
  imports: [FormsModule],
  template: RECORDS_TEMPLATE
})
export class TrafficsComponent extends RecordsListComponent<any> {
  override data = inject(DriverDataService);
  title = 'Traffic Convictions';
  singular = 'conviction';
  columns = ['Date','City','State','Charge','Penalty'];
  override fields: FieldDef[] = [{ name:'trafficDate',label:'Date (YYYY-MM-DD)',required:true },{ name:'city',label:'City' },{ name:'state',label:'State' },{ name:'charge',label:'Charge',required:true },{ name:'penalty',label:'Penalty' }];

  loadRows() { return this.data.traffics(); }
  addRow(form: any) { return this.data.addTraffic(form); }
  removeRow(row: any) { return this.data.deleteTraffic(row.uuid); }
}