import {Component, input, model} from '@angular/core';
import {Provider} from '../../models/provider';

@Component({
  selector: 'app-provider-picker',
  templateUrl: './provider-picker.html',
  styleUrl: './provider-picker.css'
})
export class ProviderPicker {
  providers = input.required<Provider[]>();
  selected = model.required<number[]>();

  readonly logoBaseUrl = "https://image.tmdb.org/t/p/w92";

  isSelected(id: number): boolean {
    return this.selected().includes(id);
  }

  toggle(id: number): void {
    this.selected.update(ids =>
      ids.includes(id) ? ids.filter(other => other !== id) : [...ids, id]
    );
  }
}
