# Comandos para Commits Lógicos

## 1. Usar o Script Automático (Recomendado)

```bash
./commit-logical.sh
```

Este script oferece menu interativo para fazer commits agrupados por funcionalidade.

---

## 2. Comando Rápido - Commit por Serviço

### Triage Service

```bash
git add triage-service/
git commit -m "feat: Implementar problema tipo triage"
```

### Medical Record Service

```bash
git add medicalrecord-service/
git commit -m "feat: Adicionar detalhes de problema ao serviço médico"
```

### Appointment Service

```bash
git add appointment-service/
git commit -m "feat: Integrar tipo de problema no agendamento"
```

### API Gateway

```bash
git add api-gateway/
git commit -m "feat: Atualizar gateway com nova funcionalidade"
```

### Config & Documentação

```bash
git add config-repo/
git commit -m "config: Atualizar configurações"

git add docs/
git commit -m "docs: Atualizar documentação"
```

---

## 3. Visualizar Mudanças Antes de Commitar

### Ver diferenças resumidas

```bash
git diff --stat main...HEAD
```

### Ver arquivos modificados

```bash
git diff --name-status main...HEAD
```

### Ver mudanças completas de um serviço

```bash
git diff main...HEAD triage-service/
git diff main...HEAD medicalrecord-service/
git diff main...HEAD appointment-service/
```

---

## 4. Staging Incremental (Adicionar Partes Específicas)

### Adicionar mudanças interativas

```bash
git add -i
# Permite escolher quais partes de cada arquivo adicionar
```

### Adicionar apenas partes específicas de um arquivo

```bash
git add -p arquivo.java
# Você confirma ou rejeita cada mudança
```

---

## 5. Rebase Interativo (Melhorar Histórico)

Se já fez commits e quer reorganizar:
```bash
git rebase -i main
# Permite reordenar, descartar ou mesclar commits
```

---

## 6. Verificar Histórico de Commits

### Ver commits da branch atual vs main
```bash
git log --oneline main..HEAD
git log --graph --oneline --all
```

### Ver mudanças de cada commit
```bash
git show HASH_DO_COMMIT
```

---

## 7. Desfazer Commits (Se Necessário)

### Desfazer último commit (mantém mudanças)
```bash
git reset --soft HEAD~1
```

### Desfazer último commit (descarta mudanças)
```bash
git reset --hard HEAD~1
```

### Remover arquivo do staging
```bash
git reset HEAD arquivo.java
```

---

## Fluxo Recomendado

1. **Verificar mudanças:**
   ```bash
   git diff --stat main...HEAD
   ```

2. **Executar script de commits lógicos:**
   ```bash
   ./commit-logical.sh
   ```

3. **Verificar commits criados:**
   ```bash
   git log --oneline main..HEAD
   ```

4. **Fazer push:**
   ```bash
   git push origin feat/problem-details
   ```

---

## Dicas

- ✅ Commits lógicos = fácil de revisar
- ✅ Uma mudança por commit = melhor histórico
- ✅ Mensagens descritivas = código documentado
- 🔄 Use `git rebase -i` para organizar antes de push
- 📝 Prefixos recomendados: `feat:`, `fix:`, `docs:`, `config:`, `ci:`
