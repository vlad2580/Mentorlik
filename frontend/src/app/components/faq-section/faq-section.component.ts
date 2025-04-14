import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

interface Faq {
  question: string;
  answer: string;
  isOpen?: boolean;
}

@Component({
  selector: 'app-faq-section',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './faq-section.component.html',
  styleUrl: './faq-section.component.scss'
})
export class FaqSectionComponent {
  faqs: Faq[] = [
    {
      question: 'Kolik stojí pomoc mentora?',
      answer: 'Cenu si určuje každý mentor sám. Může se pohybovat od 0 Kč až po několik tisíc za hodinu, podle zkušeností mentora a jeho preference. Cenu vidíte u každého mentora přímo na jeho kartě.'
    },
    {
      question: 'Jak probíhá setkání s mentorem?',
      answer: 'Po výběru mentora a domluvě termínu se s mentorem spojíte online přes videohovor nebo osobně, podle vzájemné domluvy. Setkání obvykle trvá 1 hodinu, ale záleží na individuální domluvě s mentorem.'
    },
    {
      question: 'Jak se stanu mentorem?',
      answer: 'Stačí si vytvořit profil mentora, vyplnit své zkušenosti, oblasti, ve kterých můžete pomoci, a zvolit si cenu za konzultaci. Po schválení bude váš profil viditelný pro potenciální studenty.'
    },
    {
      question: 'Mohu změnit mentora?',
      answer: 'Ano, můžete si kdykoliv vybrat jiného mentora, který lépe odpovídá vašim aktuálním potřebám nebo se kterým si více rozumíte.'
    }
  ];

  toggleFaq(faq: Faq) {
    faq.isOpen = !faq.isOpen;
  }
} 