# Sistema-de-Simulacao-de-Planejamento-Acad-mico-POO
Sistema de Simulação de Planejamento Acadêmico Orientado a Objetos

Padrão de projeto MVC

Estrutura de pastas e arquivos:
```
simulador-planejamento-academico/
 ├─ build.gradle   (ou pom.xml)
 ├─ src/
 │  ├─ main/
 │  │   └─ java/
 │  │       └─ com.simulador/
 │  │            ├─ model/
 │  │            │    ├─ domain/
 │  │            │    │    ├─ Disciplina.java
 │  │            │    │    ├─ DisciplinaObrigatoria.java
 │  │            │    │    ├─ DisciplinaEletiva.java
 │  │            │    │    ├─ DisciplinaOptativa.java
 │  │            │    │    ├─ Turma.java
 │  │            │    │    ├─ Aluno.java
 │  │            │    │    └─ Horario.java
 │  │            │    │
 │  │            │    └─ validator/
 │  │            │         ├─ ValidadorPreRequisito.java
 │  │            │         ├─ ValidadorSimples.java
 │  │            │         ├─ ValidadorLogicoAND.java
 │  │            │         └─ ValidadorLogicoOR.java
 │  │            │
 │  │            ├─ services/   # Usar para dividir a função principal em blocos menores
 │  │            │    ├─ VerifyDependencies.java
 │  │            │
 │  │            └─ controller/ # Entrada do programa
 │  │                 └─ ServicoMatricula.java
 │  │    
 │  └─ test/
 │       └─ java/
 │            └─ simulador/
 │                 ├─ resources/
 │                 │    ├─ Subjects.java
 │                 │    ├─ ClassGroup.java
 │                 │    └─ Students.java
 │                 ├─ validator/…
 │                 ├─ service/…
 │                 └─ domain/…
 │        
 └─ README.md
```

Diagrama de classes do projeto:
[Diagrama online no mermaid](https://www.mermaidchart.com/app/projects/a4e50a87-5301-4d4a-b4f5-64624b5565c8/diagrams/9d2acef6-a724-4817-81fc-8f849d2f6147/version/v0.1/edit)

```mermaid
classDiagram
namespace dominio {
    class Disciplina {
        -String codigo
        -String nome
        -int cargaHorariaSemanal
        -Set~ValidadorPreRequisito~ validadores
        +boolean requisitosAtendidos(Aluno)
    }
    class DisciplinaObrigatoria
    class DisciplinaEletiva
    class DisciplinaOptativa

    class Horario {
        -int dia
        -int inicio
        -int fim
        +boolean conflitaCom(Horario)
    }

    class Turma {
        -String id
        -Disciplina disciplina
        -int capacidadeMax
        -Set~Aluno~ matriculados
        -Horario horario
        +boolean temVagas()
    }

    class Aluno {
        -String nome
        -String matricula
        -List~Disciplina~ historicoNotas
        -Set~Turma~ planejamento
        +boolean concluiu(Disciplina)
        +int cargaHorariaPlanejada()
    }
}
Disciplina <|-- DisciplinaObrigatoria
Disciplina <|-- DisciplinaEletiva
Disciplina <|-- DisciplinaOptativa
Turma "1" --> "1" Disciplina
Horario "1" --> "1" Turma
Aluno "*" --> "*" Turma : planeja

namespace validator {
    class ValidadorPreRequisito {
        <<interface>>
        +boolean validar(Aluno, Disciplina)
    }
    class ValidadorSimples
    class ValidadorLogicoAND
    class ValidadorLogicoOR
}
ValidadorPreRequisito <|.. ValidadorSimples
ValidadorPreRequisito <|.. ValidadorLogicoAND
ValidadorPreRequisito <|.. ValidadorLogicoOR

Disciplina "1" o-- "*" ValidadorPreRequisito

namespace controller {
    class ServicoMatricula {
        -RepositorioDisciplina disciplinasRepo
        -RepositorioTurma turmasRepo
        -RepositorioAluno alunosRepo
        +void planejar(Aluno, Set~Turma~)
        +RelatorioSimulacao gerarRelatorio(Aluno)
    }
}

namespace view {
    class LoggerSimulacao {
        +void info(String)
        +void warn(String)
        +void error(String)
    }
}
ServicoMatricula --> LoggerSimulacao
```
