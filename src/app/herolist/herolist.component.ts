import { Component, Input } from '@angular/core'
import { Hero } from 'src/types/Hero';

@Component({
  selector: 'herolist',
  templateUrl: './herolist.component.html',
  styleUrls: ['./herolist.component.css']
})
export class HeroListComponent {
  @Input() heroes?: Hero[];
}
