import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DriverDataService } from './driver-data.service';
import { FieldDef, RecordsListComponent, RECORDS_TEMPLATE } from './records-list.component';

@Component({
  selector: 'app-residences',
  imports: [FormsModule],
  template: RECORDS_TEMPLATE
})
export class ResidencesComponent extends RecordsListComponent<any> {
  override data = inject(DriverDataService);
  title = 'Residency History';
  singular = 'residence';
  columns = ['Address','City','State','Postal Code'];
  override fields: FieldDef[] = [{ name:'address1',label:'Address Line 1',required:true },{ name:'city',label:'City',required:true },{ name:'state',label:'State',required:true },{ name:'postalCode',label:'Postal Code' }];

  loadRows() { return this.data.residences(); }
  addRow(form: any) { return this.data.addResidence(form); }
  removeRow(row: any) { return this.data.deleteResidence(row.uuid); }
}