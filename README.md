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
 │  │       └─ simulador/
 │  │            ├─ model/
 │  │            │    ├─ domain/
 │  │            │    │    ├─ Disciplina.java
 │  │            │    │    ├─ DisciplinaObrigatoria.java
 │  │            │    │    ├─ DisciplinaEletiva.java
 │  │            │    │    ├─ DisciplinaOptativa.java
 │  │            │    │    ├─ Turma.java
 │  │            │    │    ├─ Aluno.java
 │  │            │    │    ├─ Horario.java
 │  │            │    │  
 │  │            │    ├─ validator/
 │  │            │    │    ├─ ValidadorPreRequisito.java
 │  │            │    │    ├─ ValidadorSimples.java
 │  │            │    │    ├─ ValidadorLogicoAND.java
 │  │            │    │    └─ ValidadorLogicoOR.java
 │  │            │    │
 │  │            │    └─ separar em blocos menores controle ServicoMatricula
 │  │            │   
 │  │            ├─ repository/
 │  │            │    ├─ Repositorio.java
 │  │            │    ├─ RepositorioAluno.java
 │  │            │    ├─ RepositorioDisciplina.java
 │  │            │    └─ RepositorioTurma.java
 │  │            │
 │  │            ├─ controller/
 │  │            │    └─ ServicoMatricula.java
 │  │            │
 │  │            ├─ view/
 │  │            │    ├─ RelatorioSimulacao.java
 │  │            │    └─ LoggerSimulacao.java
 │  │            │
 │  │            └─ util/
 │  │                 └─ JsonUtil.java
 │  │
 │  ├─ resources/
 │  │     └─ data/
 │  │          ├─ disciplinas.json
 │  │          ├─ turmas.json
 │  │          └─ alunos.json
 │  └─ test/
 │       ├─ java/
 │       │    └─ simulador/
 │       │         ├─ validator/…
 │       │         ├─ service/…
 │       │         └─ domain/…
 │       └- 
 └─ README.md
```

Diagrama de classes do projeto:
![Diagrama](/Documentation/class-diagram.svg)
```mermaid
classDiagram
namespace dominio {
    class Disciplina {
        -String codigo
        -String nome
        -int cargaHorariaSemanal
        -Set~ValidadorPreRequisito~ validadores
        -Set~Disciplina~ coRequisitos
        +boolean requisitosAtendidos(Aluno)
    }
    class DisciplinaObrigatoria
    class DisciplinaEletiva
    class DisciplinaOptativa

    class Horario {
        -DiaSemana dia
        -LocalTime inicio
        -LocalTime fim
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
        -Map~Disciplina,int~ historicoNotas
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
Disciplina "1" o-- "*" Disciplina : coRequisitos

namespace repository {
    class Repositorio {
        <<interface>>
        +void salvar(T)
        +T carregar(id)
        +List~T~ listarTodos()
    }
    class RepositorioAluno
    class RepositorioDisciplina
    class RepositorioTurma
}
Repositorio <|.. RepositorioAluno
Repositorio <|.. RepositorioDisciplina
Repositorio <|.. RepositorioTurma

namespace controller {
    class ServicoMatricula {
        -RepositorioDisciplina disciplinasRepo
        -RepositorioTurma turmasRepo
        -RepositorioAluno alunosRepo
        +void planejar(Aluno, Set~Turma~)
        +RelatorioSimulacao gerarRelatorio(Aluno)
    }
}
ServicoMatricula --> RepositorioAluno
ServicoMatricula --> RepositorioDisciplina
ServicoMatricula --> RepositorioTurma

namespace view {
    class RelatorioSimulacao {
        -Aluno aluno
        -Set~Turma~ turmasPlanejadas
        -List~String~ mensagens
        +String gerar()
    }
    class LoggerSimulacao {
        +void info(String)
        +void warn(String)
    }
}
ServicoMatricula --> RelatorioSimulacao
ServicoMatricula --> LoggerSimulacao

namespace util {
    class JsonUtil {
        +<T> T fromJson(String, Class~T~)
        +String toJson(Object)
    }
    class Arquivo {
        +String ler(String caminho)
        +void escrever(String caminho, String conteudo)
    }
}
JsonUtil --> Arquivo
Repositorio --> JsonUtil
```
