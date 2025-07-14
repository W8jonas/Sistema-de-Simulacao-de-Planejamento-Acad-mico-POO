package com.simulador.controller;

import com.simulador.model.domain.*;
import com.simulador.model.validator.*;
import com.simulador.services.VerifyDependencies;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller responsável pelo processo de matrícula e interação com o usuário.
 * Gerencia o fluxo de matrícula, validações e feedback ao usuário.
 */
public class Registration {
    
    private final ServicoMatricula servicoMatricula;
    private final VerifyDependencies verifyDependencies;
    
    public Registration() {
        this.servicoMatricula = new ServicoMatricula();
        this.verifyDependencies = new VerifyDependencies();
    }
    
    /**
     * Inicializa o sistema com dados de exemplo
     */
    public void inicializarSistema() {
        // Criar disciplinas com seus códigos e nomes
        RequiredSubject calculoI = new RequiredSubject("MAT154", "Cálculo I", 4);
        RequiredSubject geometriaAnalitica = new RequiredSubject("MAT155", "Geometria Analítica e Sistemas Lineares", 4);
        RequiredSubject calculoII = new RequiredSubject("MAT156", "Cálculo II", 4);
        RequiredSubject fisicaI = new RequiredSubject("FIS073", "Física I", 4);
        RequiredSubject fisicaII = new RequiredSubject("FIS074", "Física II", 4);
        RequiredSubject labIntroFisica = new RequiredSubject("FIS122", "Laboratório de Introdução às Ciências Físicas", 2);
        RequiredSubject labFisicaI = new RequiredSubject("FIS077", "Laboratório de Física I", 2);
        RequiredSubject quimicaFundamental = new RequiredSubject("QUI125", "Química Fundamental", 4);
        RequiredSubject laboratorioQuimica = new RequiredSubject("QUI126", "Laboratório de Química", 2);
        RequiredSubject introducaoExatas = new RequiredSubject("ICE001", "Introdução às Ciências Exatas", 2);
        RequiredSubject algoritmos = new RequiredSubject("DCC119", "Algoritmos", 4);
        RequiredSubject labProgramacao = new RequiredSubject("DCC120", "Laboratório de Programação", 4);
        RequiredSubject estruturaDados = new RequiredSubject("DCC013", "Estrutura de Dados", 4);
        RequiredSubject orientacaoObjetos = new RequiredSubject("DCC025", "Orientação a Objetos", 4);
        RequiredSubject modelagemSistemas = new RequiredSubject("DCC117", "Modelagem de Sistemas", 4);
        RequiredSubject programacaoWeb = new RequiredSubject("DCC121", "Programação Web", 4);
        RequiredSubject estruturaDadosII = new RequiredSubject("DCC012", "Estrutura de Dados II", 4);
        RequiredSubject teoriaGrafos = new RequiredSubject("DCC059", "Teoria dos Grafos", 4);
        RequiredSubject metodologiaCientifica = new RequiredSubject("DCC123", "Metodologia Científica em Computação", 4);
        RequiredSubject monografiaFinal = new RequiredSubject("DCC110", "Monografia Final em Computação", 4);
        
        // Configurar pré-requisitos usando diferentes tipos de validadores
        // Cálculo II: requer MAT154 E MAT155 (ValidadorLogicoAND)
        List<ValidadorPreRequisito> preRequisitosCalculoII = Arrays.asList(
            new ValidadorSimples(calculoI),
            new ValidadorSimples(geometriaAnalitica)
        );
        calculoII.setValidadores(new ValidadorLogicoAND(preRequisitosCalculoII));
        
        // Física I: requer MAT154 E MAT155 (ValidadorLogicoAND)
        List<ValidadorPreRequisito> preRequisitosFisicaI = Arrays.asList(
            new ValidadorSimples(calculoI),
            new ValidadorSimples(geometriaAnalitica)
        );
        fisicaI.setValidadores(new ValidadorLogicoAND(preRequisitosFisicaI));
        
        // Física II: requer FIS073 E MAT156 (ValidadorLogicoAND)
        List<ValidadorPreRequisito> preRequisitosFisicaII = Arrays.asList(
            new ValidadorSimples(fisicaI),
            new ValidadorSimples(calculoII)
        );
        fisicaII.setValidadores(new ValidadorLogicoAND(preRequisitosFisicaII));
        
        // Laboratório de Química: requer QUI125 (ValidadorSimples)
        laboratorioQuimica.setValidadores(new ValidadorSimples(quimicaFundamental));
        
        // Laboratório de Física I: requer FIS122 (ValidadorSimples)
        labFisicaI.setValidadores(new ValidadorSimples(labIntroFisica));
        
        // Estrutura de Dados: requer DCC119 E DCC120 (ValidadorLogicoAND)
        List<ValidadorPreRequisito> preRequisitosEstruturaDados = Arrays.asList(
            new ValidadorSimples(algoritmos),
            new ValidadorSimples(labProgramacao)
        );
        estruturaDados.setValidadores(new ValidadorLogicoAND(preRequisitosEstruturaDados));
        
        // Modelagem de Sistemas: requer DCC025 (ValidadorSimples)
        modelagemSistemas.setValidadores(new ValidadorSimples(orientacaoObjetos));
        
        // Programação Web: requer DCC119 E DCC120 (ValidadorLogicoAND)
        List<ValidadorPreRequisito> preRequisitosProgramacaoWeb = Arrays.asList(
            new ValidadorSimples(algoritmos),
            new ValidadorSimples(labProgramacao)
        );
        programacaoWeb.setValidadores(new ValidadorLogicoAND(preRequisitosProgramacaoWeb));
        
        // Teoria dos Grafos: requer DCC012 (ValidadorSimples)
        teoriaGrafos.setValidadores(new ValidadorSimples(estruturaDadosII));
        
        // Metodologia Científica: requer múltiplas disciplinas (ValidadorLogicoAND)
        List<ValidadorPreRequisito> preRequisitosMetodologia = Arrays.asList(
            new ValidadorSimples(calculoI),
            new ValidadorSimples(geometriaAnalitica),
            new ValidadorSimples(calculoII),
            new ValidadorSimples(fisicaI),
            new ValidadorSimples(algoritmos),
            new ValidadorSimples(labProgramacao),
            new ValidadorSimples(estruturaDados),
            new ValidadorSimples(orientacaoObjetos)
        );
        metodologiaCientifica.setValidadores(new ValidadorLogicoAND(preRequisitosMetodologia));
        
        // Monografia Final: requer DCC123 (ValidadorSimples)
        monografiaFinal.setValidadores(new ValidadorSimples(metodologiaCientifica));
        
        // Registrar todas as disciplinas
        servicoMatricula.registrarDisciplina(calculoI);
        servicoMatricula.registrarDisciplina(geometriaAnalitica);
        servicoMatricula.registrarDisciplina(calculoII);
        servicoMatricula.registrarDisciplina(fisicaI);
        servicoMatricula.registrarDisciplina(fisicaII);
        servicoMatricula.registrarDisciplina(labIntroFisica);
        servicoMatricula.registrarDisciplina(labFisicaI);
        servicoMatricula.registrarDisciplina(quimicaFundamental);
        servicoMatricula.registrarDisciplina(laboratorioQuimica);
        servicoMatricula.registrarDisciplina(introducaoExatas);
        servicoMatricula.registrarDisciplina(algoritmos);
        servicoMatricula.registrarDisciplina(labProgramacao);
        servicoMatricula.registrarDisciplina(estruturaDados);
        servicoMatricula.registrarDisciplina(orientacaoObjetos);
        servicoMatricula.registrarDisciplina(modelagemSistemas);
        servicoMatricula.registrarDisciplina(programacaoWeb);
        servicoMatricula.registrarDisciplina(estruturaDadosII);
        servicoMatricula.registrarDisciplina(teoriaGrafos);
        servicoMatricula.registrarDisciplina(metodologiaCientifica);
        servicoMatricula.registrarDisciplina(monografiaFinal);
        
        // Criar horários organizados para testar conflitos
        // Segunda-feira
        Schedule seg8h = new Schedule(1, 8, 10);   // Segunda, 8h-10h
        Schedule seg10h = new Schedule(1, 10, 12); // Segunda, 10h-12h
        Schedule seg14h = new Schedule(1, 14, 16); // Segunda, 14h-16h
        Schedule seg16h = new Schedule(1, 16, 18); // Segunda, 16h-18h
        
        // Terça-feira
        Schedule ter8h = new Schedule(2, 8, 10);   // Terça, 8h-10h
        Schedule ter10h = new Schedule(2, 10, 12); // Terça, 10h-12h
        Schedule ter14h = new Schedule(2, 14, 16); // Terça, 14h-16h
        Schedule ter16h = new Schedule(2, 16, 18); // Terça, 16h-18h
        
        // Quarta-feira
        Schedule qua8h = new Schedule(3, 8, 10);   // Quarta, 8h-10h
        Schedule qua10h = new Schedule(3, 10, 12); // Quarta, 10h-12h
        Schedule qua14h = new Schedule(3, 14, 16); // Quarta, 14h-16h
        Schedule qua16h = new Schedule(3, 16, 18); // Quarta, 16h-18h
        
        // Quinta-feira
        Schedule qui8h = new Schedule(4, 8, 10);   // Quinta, 8h-10h
        Schedule qui10h = new Schedule(4, 10, 12); // Quinta, 10h-12h
        Schedule qui14h = new Schedule(4, 14, 16); // Quinta, 14h-16h
        Schedule qui16h = new Schedule(4, 16, 18); // Quinta, 16h-18h
        
        // Sexta-feira
        Schedule sex8h = new Schedule(5, 8, 10);   // Sexta, 8h-10h
        Schedule sex10h = new Schedule(5, 10, 12); // Sexta, 10h-12h
        Schedule sex14h = new Schedule(5, 14, 16); // Sexta, 14h-16h
        Schedule sex16h = new Schedule(5, 16, 18); // Sexta, 16h-18h
        
        // Criar turmas com horários que permitam testar conflitos
        // Disciplinas básicas (sem pré-requisitos)
        ClassGroup turmaCalculoI_1 = new ClassGroup("MAT154-01", calculoI, 30, Arrays.asList(seg8h));
        ClassGroup turmaCalculoI_2 = new ClassGroup("MAT154-02", calculoI, 30, Arrays.asList(seg10h)); // Conflito com MAT154-01
        ClassGroup turmaGeometria_1 = new ClassGroup("MAT155-01", geometriaAnalitica, 25, Arrays.asList(ter8h));
        ClassGroup turmaQuimicaFund_1 = new ClassGroup("QUI125-01", quimicaFundamental, 25, Arrays.asList(qua8h));
        ClassGroup turmaAlgoritmos_1 = new ClassGroup("DCC119-01", algoritmos, 20, Arrays.asList(qui8h));
        ClassGroup turmaLabProg_1 = new ClassGroup("DCC120-01", labProgramacao, 15, Arrays.asList(sex8h));
        ClassGroup turmaOrientacaoObj_1 = new ClassGroup("DCC025-01", orientacaoObjetos, 20, Arrays.asList(seg14h));
        
        // Disciplinas com pré-requisitos simples
        ClassGroup turmaCalculoII_1 = new ClassGroup("MAT156-01", calculoII, 25, Arrays.asList(ter14h));
        ClassGroup turmaFisicaI_1 = new ClassGroup("FIS073-01", fisicaI, 25, Arrays.asList(qua14h));
        ClassGroup turmaLabQuimica_1 = new ClassGroup("QUI126-01", laboratorioQuimica, 15, Arrays.asList(qui14h));
        ClassGroup turmaLabIntroFisica_1 = new ClassGroup("FIS122-01", labIntroFisica, 20, Arrays.asList(sex8h));
        ClassGroup turmaLabFisicaI_1 = new ClassGroup("FIS077-01", labFisicaI, 15, Arrays.asList(sex10h));
        ClassGroup turmaEstruturaDados_1 = new ClassGroup("DCC013-01", estruturaDados, 20, Arrays.asList(sex14h));
        ClassGroup turmaModelagem_1 = new ClassGroup("DCC117-01", modelagemSistemas, 15, Arrays.asList(seg16h));
        ClassGroup turmaProgWeb_1 = new ClassGroup("DCC121-01", programacaoWeb, 15, Arrays.asList(ter16h));
        
        // Disciplinas avançadas
        ClassGroup turmaFisicaII_1 = new ClassGroup("FIS074-01", fisicaII, 20, Arrays.asList(qua16h));
        ClassGroup turmaEstruturaDadosII_1 = new ClassGroup("DCC012-01", estruturaDadosII, 15, Arrays.asList(qui16h));
        ClassGroup turmaTeoriaGrafos_1 = new ClassGroup("DCC059-01", teoriaGrafos, 15, Arrays.asList(sex16h));
        ClassGroup turmaMetodologia_1 = new ClassGroup("DCC123-01", metodologiaCientifica, 10, Arrays.asList(seg8h)); // Conflito com MAT154-01
        ClassGroup turmaMonografia_1 = new ClassGroup("DCC110-01", monografiaFinal, 5, Arrays.asList(ter8h)); // Conflito com MAT155-01
        
        // Turmas extras para testar conflitos
        ClassGroup turmaCalculoI_3 = new ClassGroup("MAT154-03", calculoI, 30, Arrays.asList(seg8h)); // Conflito com MAT154-01
        ClassGroup turmaGeometria_2 = new ClassGroup("MAT155-02", geometriaAnalitica, 25, Arrays.asList(seg10h)); // Conflito com MAT154-02
        ClassGroup turmaQuimicaFund_2 = new ClassGroup("QUI125-02", quimicaFundamental, 25, Arrays.asList(seg8h)); // Conflito com MAT154-01
        
        // Registrar turmas
        servicoMatricula.registrarTurma(turmaCalculoI_1);
        servicoMatricula.registrarTurma(turmaCalculoI_2);
        servicoMatricula.registrarTurma(turmaCalculoI_3);
        servicoMatricula.registrarTurma(turmaGeometria_1);
        servicoMatricula.registrarTurma(turmaGeometria_2);
        servicoMatricula.registrarTurma(turmaQuimicaFund_1);
        servicoMatricula.registrarTurma(turmaQuimicaFund_2);
        servicoMatricula.registrarTurma(turmaCalculoII_1);
        servicoMatricula.registrarTurma(turmaFisicaI_1);
        servicoMatricula.registrarTurma(turmaFisicaII_1);
        servicoMatricula.registrarTurma(turmaLabQuimica_1);
        servicoMatricula.registrarTurma(turmaLabIntroFisica_1);
        servicoMatricula.registrarTurma(turmaLabFisicaI_1);
        servicoMatricula.registrarTurma(turmaAlgoritmos_1);
        servicoMatricula.registrarTurma(turmaLabProg_1);
        servicoMatricula.registrarTurma(turmaEstruturaDados_1);
        servicoMatricula.registrarTurma(turmaOrientacaoObj_1);
        servicoMatricula.registrarTurma(turmaModelagem_1);
        servicoMatricula.registrarTurma(turmaProgWeb_1);
        servicoMatricula.registrarTurma(turmaEstruturaDadosII_1);
        servicoMatricula.registrarTurma(turmaTeoriaGrafos_1);
        servicoMatricula.registrarTurma(turmaMetodologia_1);
        servicoMatricula.registrarTurma(turmaMonografia_1);
        
        // Criar um aluno para testar todos os cenários
        Student aluno = new Student("João Silva", "202365082A", 20);
        
        // Cenário inicial: aluno cursou disciplinas básicas + ICE001
        aluno.addCompletedSubject(calculoI, 8.5);
        aluno.addCompletedSubject(geometriaAnalitica, 7.0);
        aluno.addCompletedSubject(algoritmos, 9.0);
        aluno.addCompletedSubject(introducaoExatas, 8.0); // ICE001 já cursada
        aluno.addCompletedSubject(labIntroFisica, 8.0); // FIS122 já cursada
        
        // Registrar aluno
        servicoMatricula.registrarAluno(aluno);
    }
    
    /**
     * Processa a matrícula de um aluno em turmas específicas
     */
    public RelatorioSimulacao processarMatricula(String matriculaAluno, List<String> idsTurmas) {
        Student aluno = servicoMatricula.getAluno(matriculaAluno);
        if (aluno == null) {
            RelatorioSimulacao relatorio = new RelatorioSimulacao(null);
            relatorio.adicionarErro("Aluno não encontrado: " + matriculaAluno);
            return relatorio;
        }
        
        Set<ClassGroup> turmasDesejadas = new HashSet<>();
        for (String idTurma : idsTurmas) {
            ClassGroup turma = servicoMatricula.getTurma(idTurma);
            if (turma == null) {
                RelatorioSimulacao relatorio = new RelatorioSimulacao(aluno);
                relatorio.adicionarErro("Turma não encontrada: " + idTurma);
                return relatorio;
            }
            turmasDesejadas.add(turma);
        }
        
        return servicoMatricula.planejar(aluno, turmasDesejadas);
    }
    
    /**
     * Lista todas as disciplinas disponíveis
     */
    public List<Subject> listarDisciplinas() {
        return servicoMatricula.getDisciplinasDisponiveis();
    }
    
    /**
     * Lista todas as turmas disponíveis
     */
    public List<ClassGroup> listarTurmas() {
        return servicoMatricula.getTurmasDisponiveis();
    }
    
    /**
     * Lista turmas de uma disciplina específica
     */
    public List<ClassGroup> listarTurmasPorDisciplina(String codigoDisciplina) {
        return servicoMatricula.getTurmasDisponiveis().stream()
                .filter(turma -> turma.getSubject().getCode().equals(codigoDisciplina))
                .collect(Collectors.toList());
    }
    
    /**
     * Busca um aluno por matrícula
     */
    public Student buscarAluno(String matricula) {
        return servicoMatricula.getAluno(matricula);
    }
    
    /**
     * Gera relatório completo de um aluno
     */
    public RelatorioSimulacao gerarRelatorioAluno(String matricula) {
        Student aluno = servicoMatricula.getAluno(matricula);
        if (aluno == null) {
            RelatorioSimulacao relatorio = new RelatorioSimulacao(null);
            relatorio.adicionarErro("Aluno não encontrado: " + matricula);
            return relatorio;
        }
        
        return servicoMatricula.gerarRelatorio(aluno);
    }
    
    /**
     * Verifica se um aluno pode se matricular em uma disciplina específica
     */
    public RelatorioSimulacao verificarElegibilidade(String matricula, String codigoDisciplina) {
        Student aluno = servicoMatricula.getAluno(matricula);
        Subject disciplina = servicoMatricula.getDisciplina(codigoDisciplina);
        
        if (aluno == null) {
            RelatorioSimulacao relatorio = new RelatorioSimulacao(null);
            relatorio.adicionarErro("Aluno não encontrado: " + matricula);
            return relatorio;
        }
        
        if (disciplina == null) {
            RelatorioSimulacao relatorio = new RelatorioSimulacao(aluno);
            relatorio.adicionarErro("Disciplina não encontrada: " + codigoDisciplina);
            return relatorio;
        }
        
        RelatorioSimulacao relatorio = new RelatorioSimulacao(aluno);
        relatorio.adicionarInfo("Verificando elegibilidade para: " + disciplina.getCode() + " - " + disciplina.getName());
        
        // Verificar se já cursou a disciplina
        if (aluno.hasCompletedSubject(disciplina)) {
            relatorio.adicionarAviso("Aluno já cursou esta disciplina com nota: " + aluno.getGrade(disciplina));
        }
        
        // Verificar se já está no planejamento
        if (aluno.getFuturePlanning().contains(disciplina)) {
            relatorio.adicionarAviso("Disciplina já está no planejamento futuro");
        }
        
        // Verificar pré-requisitos
        if (!disciplina.requisitosAtendidos(aluno)) {
            relatorio.adicionarErro("Pré-requisitos não atendidos para " + disciplina.getCode() + " - " + disciplina.getName());
        }
        
        // Verificar carga horária
        int horasAtuais = aluno.getFuturePlanningWeeklyHours();
        int horasDisciplina = disciplina.getWeeklyHours();
        int horasRestantes = aluno.getRemainingWeeklyHours();
        
        relatorio.adicionarInfo("Carga horária atual: " + horasAtuais + "h");
        relatorio.adicionarInfo("Carga horária da disciplina: " + horasDisciplina + "h");
        relatorio.adicionarInfo("Horas restantes disponíveis: " + horasRestantes + "h");
        
        if (horasDisciplina > horasRestantes) {
            relatorio.adicionarErro("Disciplina excede o limite de horas semanais disponíveis");
        }
        
        // Definir sucesso se não há erros
        if (!relatorio.temErros()) {
            relatorio.setSucesso(true);
        }
        
        return relatorio;
    }
    
    /**
     * Obtém o serviço de matrícula para uso direto
     */
    public ServicoMatricula getServicoMatricula() {
        return servicoMatricula;
    }
}
