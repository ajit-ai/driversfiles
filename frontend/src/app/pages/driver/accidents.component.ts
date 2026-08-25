import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DriverDataService } from './driver-data.service';
import { FieldDef, RecordsListComponent, RECORDS_TEMPLATE } from './records-list.component';

@Component({
  selector: 'app-accidents',
  imports: [FormsModule],
  template: RECORDS_TEMPLATE
})
export class AccidentsComponent extends RecordsListComponent<any> {
  override data = inject(DriverDataService);
  title = 'Accidents';
  singular = 'accident';
  columns = ['Date','Type','Nature','At Fault','Injuries'];
  override fields: FieldDef[] = [{ name:'accidentDate',label:'Date (YYYY-MM-DD)',required:true },{ name:'type',label:'Type',required:true },{ name:'nature',label:'Nature',required:true },{ name:'atFault',label:'At Fault',type:'checkbox' },{ name:'fatalities',label:'Fatalities',type:'checkbox' },{ name:'injuries',label:'Injuries',type:'checkbox' }];

  loadRows() { return this.data.accidents(); }
  addRow(form: any) { return this.data.addAccident(form); }
  removeRow(row: any) { return this.data.deleteAccident(row.uuid); }
}