<<<<<<< HEAD
# HackathonONE (Oracle + Alura)

# 📊 SentimentoAPI - Análise de Sentimento com IA e Microserviços

> **Hackathon MVP**: Solução automatizada para classificação de feedbacks de clientes utilizando Processamento de Linguagem Natural (NLP).

## 💡 Sobre o Projeto

Empresas recebem milhares de comentários diariamente e não conseguem ler todos manualmente. A **SentimentoAPI** resolve esse problema identificando automaticamente se um comentário é **Positivo** ou **Negativo**, permitindo:

  * Priorização de atendimento a clientes insatisfeitos.
  * Monitoramento da imagem da marca em tempo real.
  * Geração de métricas de qualidade (CSAT/NPS).

-----

## 🚀 Tecnologias

<div>
  <img src="https://img.shields.io/badge/Java-17-blue?style=for-the-badge&logo=java&logoColor=white">
  <img src="https://img.shields.io/badge/Spring_Boot-3.0.6-green?style=for-the-badge&logo=springboot&logoColor=white">
  <img src="https://img.shields.io/badge/Python-3.9-blue?style=for-the-badge&logo=python&logoColor=white">
  <img src="https://img.shields.io/badge/Flask-000000?style=for-the-badge&logo=flask&logoColor=white">
  <img src="https://img.shields.io/badge/Scikit--learn-FF9800?style=for-the-badge&logo=scikit-learn&logoColor=white">
  <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white">
  <img src="https://img.shields.io/badge/Joblib-0095D9?style=for-the-badge&logo=python&logoColor=white">
  <img src="https://img.shields.io/badge/TensorFlow-FF6F00?style=for-the-badge&logo=tensorflow&logoColor=white">
  <img src="https://img.shields.io/badge/H2-2.1.214-blue?style=for-the-badge&logo=h2&logoColor=white">
  <img src="https://img.shields.io/badge/PostgreSQL-42.5.6-blue?style=for-the-badge&logo=postgresql&logoColor=white">
  <img src="https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white">
  <img src="https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white">
</div>

<p>Este projeto utiliza as seguintes tecnologias:</p>
<ul>
  <li><strong>Java 17</strong>: Linguagem de programação utilizada para desenvolver o backend.</li>
  <li><strong>Spring Boot</strong>: Framework Java utilizado para desenvolver a API do sistema.</li>
  <li><strong>Python 3.9</strong>: Linguagem de programação usada no microserviço de Data Science.</li>
  <li><strong>Flask</strong>: Framework Python para construir a API que hospeda o modelo de Machine Learning.</li>
  <li><strong>Scikit-learn</strong>: Biblioteca Python para machine learning, utilizada para treinar o modelo de sentimento.</li>
  <li><strong>Docker</strong>: Plataforma para automatizar a implantação de aplicações em containers, facilitando o desenvolvimento e a execução do projeto em diferentes ambientes.</li>
  <li><strong>Joblib</strong>: Biblioteca Python para serialização do modelo treinado, permitindo seu carregamento eficiente no ambiente de produção.</li>
  <li><strong>TensorFlow</strong>: Embora o modelo atual utilize o Scikit-learn, o TensorFlow pode ser utilizado para treinamento mais avançado, como redes neurais.</li>
  <li><strong>H2</strong>: Banco de dados em memória utilizado para testes e desenvolvimento.</li>
  <li><strong>PostgreSQL</strong>: Sistema de gerenciamento de banco de dados relacional utilizado em produção.</li>
  <li><strong>GitHub</strong>: Plataforma de hospedagem de código, utilizada para versionamento do projeto e colaboração entre os desenvolvedores.</li>
  <li><strong>Postman</strong>: Ferramenta usada para testar as APIs de forma rápida e eficaz, permitindo simular requisições HTTP e visualizar as respostas.</li>
</ul>


## 📊 Estado do Projeto

![Progresso](https://img.shields.io/badge/Progresso-40%25-red?style=for-the-badge&labelColor=000000&color=FF0000&logo=github)

## 🏗️ Arquitetura Técnica

### Explicação do Diagrama Mermaid:

- **Usuário Envia Texto**: O usuário envia um texto para a API.
- **API Spring Boot**: A API recebe a requisição via **POST** e a envia para o microserviço Python.
- **Microserviço Python**: O microserviço Python realiza a análise de sentimento e retorna a previsão.
- **Resposta da API**: A previsão de sentimento é retornada ao usuário via API.
- **Opções de Ações**: O usuário pode optar por atualizar ou excluir a previsão de sentimento.

 ```mermaid
graph LR
    A[Usuário Envia Texto] --> B{API Spring Boot}
    B --> C[Requisição POST para Python]
    C --> D[Microserviço Python]
    D --> E[Retorno da Previsão de Sentimento]
    E --> F[Resposta da API com Previsão]
    F --> G[Usuário Recebe Previsão]
    G --> H{Usuário Opções}
    H -->|Atualizar| I[PUT Atualiza Sentimento]
    H -->|Excluir| J[DELETE Exclui Sentimento]
```

### Data Science

  * **Python 3.9**
  * **Scikit-learn** (Modelo de Regressão Logística)
  * **Pandas** (Manipulação de dados)
  * **TF-IDF Vectorizer** (Processamento de texto)
  * **Joblib** (Serialização do modelo)
  * **Flask** (Exposição do modelo como API)

### Infraestrutura

  * **Docker** & **Docker Compose**

-----

## 🚀 Como Executar

### Pré-requisito: Treinamento do Modelo

Antes de subir os containers, é necessário gerar o arquivo binário do modelo de IA.

1.  Acesse a pasta `ds-python`.
2.  Execute o script de treinamento (necessário Python instalado localmente ou executar dentro de um container isolado):
    ```bash
    python treinar_modelo.py
    ```
    *Isso criará o arquivo `sentiment_model.joblib`.*

### Opção 1: Rodando com Docker (Recomendado)

Na raiz do projeto (onde está o `docker-compose.yml`):

```bash
docker-compose up --build
```

Aguarde até ver as mensagens de log indicando que ambos os serviços iniciaram.

  * **API Principal:** `http://localhost:8081`
  * **Serviço de IA (Interno):** `http://localhost:5000`

### Opção 2: Rodando Manualmente

**1. Subir o Serviço de Data Science:**

```bash
cd ds-python
pip install -r requirements.txt
python app_python.py
# O serviço rodará na porta 5000
```

**2. Subir o Back-End Java:**

```bash
cd backend-java
./mvnw spring-boot:run
# O serviço rodará na porta 8080
```

-----

## 🔌 Documentação da API

### Endpoint: Classificar Sentimento

Analisa um texto e retorna a previsão do sentimento e a confiança do modelo.

  * **URL:** `/sentiment`
  * **Método:** `POST`
  * **Content-Type:** `application/json`

#### Exemplo de Requisição (Body)

```json
{
  "text": "O produto chegou rápido e a qualidade é excelente!"
}
```

#### Exemplo de Resposta (Sucesso - 200 OK)

```json
{
  "previsao": "Positivo",
  "probabilidade": 0.92
}
```

#### Exemplo de Resposta (Negativo)

```json
{
  "text": "Péssimo atendimento, nunca mais compro."
}
```

**Saída:**

```json
{
  "previsao": "Negativo",
  "probabilidade": 0.88
}
```

#### Tratamento de Erros

  * **400 Bad Request:** Se o campo `text` estiver vazio ou nulo.
  * **500 Internal Server Error:** Caso o serviço de IA esteja indisponível.

-----

## 🧠 Detalhes do Modelo de Data Science

Para este MVP, optamos por uma abordagem clássica e eficiente de Machine Learning Supervisionado:

1.  **Pré-processamento:** Limpeza básica de texto.
2.  **Vetorização (TF-IDF):** Transformamos os textos em números baseados na frequência e importância das palavras no corpus.
3.  **Algoritmo (Regressão Logística):** Escolhido por ser rápido, interpretável e apresentar excelente desempenho para classificação binária de textos curtos.
4.  **Métricas:** O modelo retorna não apenas a classe (Pos/Neg), mas a probabilidade (`predict_proba`), permitindo definir *thresholds* de confiança.

-----

## 🔮 Próximos Passos (Roadmap)

  * [ ] Implementar banco de dados (H2/Postgres) para histórico de requisições.
  * [ ] Dashboard visual para acompanhar a média de sentimento.
  * [ ] Suporte a análise de sentimento em múltiplos idiomas.
  * [ ] Autenticação via API Key/JWT.

-----

*Desenvolvido pela Equipe para o Hackathon 2025.*

-----


# Descrição Geral

Setor de negócio

Atendimento ao cliente / Marketing / Operações — empresas que coletam opiniões de clientes (avaliações, comentários em redes sociais, pesquisas de satisfação) e querem entender rapidamente se o sentimento é positivo, neutro ou negativo.

Descrição do projeto

Criar uma API simples que recebe textos (comentários, avaliações ou tweets), aplica um modelo de Data Science para classificar o sentimento (Atrasado / Pontual → neste caso: Positivo / Neutro / Negativo ou binário Positivo / Negativo) e retorna o resultado em formato JSON, permitindo que aplicações consumam essa predição automaticamente.

Necessidade do cliente (explicação não técnica)

Um cliente (empresa) recebe muitos comentários e não consegue ler tudo manualmente. Ele quer:

saber rapidamente se os clientes estão reclamando ou elogiando;

priorizar respostas a comentários negativos;

medir a satisfação ao longo do tempo.

Esse projeto oferece uma solução automática para classificar mensagens e gerar informações acionáveis.

Validação de mercado

Analisar sentimento é útil para:

acelerar atendimento ao cliente (identificar urgências);

monitorar campanhas de marketing;

comparar a imagem da marca ao longo do tempo.

Mesmo uma solução simples (modelo básico) tem valor: empresas pequenas e médias usam ferramentas similares para entender feedbacks sem equipe dedicada.

Expectativa para este hackathon

Público: alunos sem experiência profissional na área de tecnologia, que estudaram Back-end (Java, Spring, REST, persistência) e Data Science (Python, Pandas, scikit-learn, notebooks).

Objetivo: entregar um MVP funcional que demonstre integração entre DS e Back-end: um notebook com o modelo + uma API que carrega esse modelo e responde a requisições.

Escopo recomendado: classificação binária (Positivo / Negativo) ou trinária (Positivo / Neutro / Negativo) com um modelo simples — por exemplo, usar TF-IDF (uma técnica que transforma o texto em números, mostrando quais palavras são mais importantes) junto com Regressão Logística (um modelo de aprendizado de máquina que aprende a diferenciar sentimentos).

Entregáveis desejados

Notebook (Jupyter/Colab) do time de Data Science contendo:

Exploração e limpeza dos dados (EDA);

Transformação dos textos em números com TF-IDF;

Treinamento de modelo supervisionado (ex.: Logistic Regression, Naive Bayes);

Métricas de desempenho (Acurácia, Precisão, Recall, F1-score);

Serialização do modelo (joblib/pickle).

Aplicação Back-End (preferencialmente Spring Boot em Java):

API que consome o modelo (diretamente ou chamando o microserviço DS) e expõe endpoint /sentiment;

Endpoint que recebe informações e retorna a previsão do modelo;

Logs e tratamento de erros.

Documentação mínima (README):

Como executar o modelo e a API;

Exemplos de requisição e resposta (JSON);

Dependências e versões das ferramentas.

Demonstração funcional (Apresentação curta):

Mostrar a API em ação (via Postman, cURL ou interface simples);

Explicar como o modelo chega à previsão.

Funcionalidades exigidas (MVP)

O serviço deve expor um endpoint que retorna a classificação do sentimento e a probabilidade associada a essa classificação. Exemplo: POST /sentiment — aceita JSON com campo text e retorna: { "previsao": "Positivo", "probabilidade": 0.87 }

Modelo treinado e carregável: o back-end deve conseguir usar o modelo (carregando arquivo) ou fazer uma requisição a um microserviço DS que implemente a predição.

Validação de input: checar se text existe e tem comprimento mínimo; retornar erro amigável em caso contrário.

Resposta clara: label (+ probabilidade em 0–1) e mensagem de erro quando aplicável.

Exemplos de uso: Postman/cURL com 3 exemplos reais (positivo, neutro, negativo).

README explicando como rodar (passos simples) e como testar o endpoint.

Funcionalidades opcionais

Endpoint GET /stats com estatísticas simples (percentual de positivos/negativos nos últimos X comentários).

Persistência: salvar requisições e previsões em banco (H2 ou Postgres) para análises posteriores.

Explicabilidade básica: retornar as palavras mais influentes na predição (ex.: "top features": ["ótimo", "atendimento"]).

Interface simples (Streamlit / página web) para testar texto livremente.

Batch processing: endpoint para enviar vários textos em CSV e receber previsões em lote.

Versão multilingue (Português + Espanhol) ou opção para trocar o threshold de probabilidade.

Containerização com Docker e docker-compose para subir DS + BE juntos.

Testes automatizados: alguns testes unitários e um teste de integração simples.

Orientações técnicas para alunos

Recomendamos cuidado quando da utilização limitada das instâncias fornecidas pelos serviços always free da OCI, para não acarretar em gastos adicionais.

Time de Data Science

Cada equipe deve escolher ou montar seu próprio conjunto de dados de comentários, avaliações ou postagens que possam ser usados para análise de sentimento (ex.: reviews públicos, tweets, avaliações de produtos etc.).

use Python, Pandas para ler/limpar dados;

crie um modelo simples (TF-IDF + LogisticRegression do scikit-learn);

salve o pipeline e o modelo com joblib.dump.

Coloque tudo em um notebook bem comentado.

Time de Back-End

crie uma API REST (em Java com Spring Boot).

Implementar um endpoint (ex: /sentiment ) que recebe a avaliação e retorna o sentimento

Integrar o modelo de Data Science:

via microserviço Python (FastAPI/Flask), ou

carregando o modelo exportado (ONNX, para times Java avançados).

Validar entradas e retornar respostas JSON consistentes.

Contrato de integração (definido entre DS e BE)

Recomendamos definir desde o início o formato JSON de entrada e saída. Segue um exemplo:

{"text": "…"} →

{

"previsao":"Positivo",

Esse é um projeto clássico e excelente para um Hackathon, pois demonstra perfeitamente a integração entre sistemas.

Como Java não consegue ler nativamente arquivos serializados do Python (`.joblib` ou `.pkl`) de forma simples, a arquitetura padrão da indústria para esse cenário é a de **Microserviços**.

Aqui está o roteiro completo e o código para o seu **MVP**, dividido em duas partes: o **Serviço de Data Science (Python)** e a **API Principal (Java Spring Boot)**.

[Image of microservices architecture pattern]

-----

### 1\. Time de Data Science (Python)

O objetivo aqui é treinar o modelo e expô-lo via uma API leve (Flask ou FastAPI) para que o Java possa consultá-lo.

#### A. O Notebook de Treinamento (`treinar_modelo.py`)

Este script simula o notebook. Ele cria dados fictícios, treina o modelo usando **TF-IDF** e **Regressão Logística**, e salva o arquivo.

**Conceito Técnico:**
O TF-IDF ($Term Frequency - Inverse Document Frequency$) transforma texto em números. A fórmula básica para o peso de um termo é:
$$w_{i,j} = tf_{i,j} \times \log(\frac{N}{df_i})$$
Onde a Regressão Logística usa esses pesos para traçar uma linha divisória entre "Positivo" e "Negativo".

```python
# treinar_modelo.py
import pandas as pd
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.linear_model import LogisticRegression
from sklearn.pipeline import make_pipeline
from sklearn.model_selection import train_test_split
from sklearn.metrics import classification_report
import joblib

# 1. Dataset Fictício (Em um caso real, carregue um CSV)
data = {
    'texto': [
        "Adorei o produto, muito bom", "Excelente atendimento", "Entrega rápida e perfeita",
        "Gostei bastante, recomendo", "Maravilhoso", "Muito satisfatório",
        "O produto chegou quebrado", "Péssimo serviço", "Não recomendo a ninguém",
        "Demorou muito e veio errado", "Horrível", "Estou muito insatisfeito",
        "O atendimento foi normal", "Nada de especial", "Chegou no prazo mas a caixa amassou"
    ],
    'sentimento': [
        "Positivo", "Positivo", "Positivo", "Positivo", "Positivo", "Positivo",
        "Negativo", "Negativo", "Negativo", "Negativo", "Negativo", "Negativo",
        "Neutro", "Neutro", "Neutro"
    ]
}

df = pd.DataFrame(data)

# 2. Separação de dados
X = df['texto']
y = df['sentimento']

# 3. Criação do Pipeline (TF-IDF + Regressão Logística)
model = make_pipeline(TfidfVectorizer(), LogisticRegression())

# 4. Treinamento
model.fit(X, y)

# 5. Teste rápido
print("Acurácia no treino:", model.score(X, y))

# 6. Serialização (Salvar o modelo)
joblib.dump(model, 'sentiment_model.joblib')
print("Modelo salvo como 'sentiment_model.joblib'")
```

#### B. A API do Modelo (`app_python.py`)

Esta é a API que o Spring Boot vai chamar. Ela carrega o arquivo `.joblib` e responde a requisições. Vamos usar **Flask** por ser simples.

```python
from flask import Flask, request, jsonify
import joblib

app = Flask(__name__)

try:
    data = joblib.load('modelo_b2w_rating_sentimento.pkl')
    model = data['model']
    vectorizer = data['vectorizer']
    print("Modelo carregado com sucesso!")

except Exception as e:
    print("Erro: Erro ao carregar o modelo.")
    model = None
    vectorizer = None

LABEL_MAP = {
    "negative": "Negativo",
    "neutral": "Neutro",    
    "positive": "Positivo"
}



@app.route('/predict', methods=['POST'])
def predict():
    try:
        dados = request.get_json()
        texto = dados.get('text') if dados else None

        if not texto or len(texto.strip()) < 5:
            return jsonify({"erro": "Texto não fornecido ou muito curto."}), 400

        X = vectorizer.transform([texto])

        prediction_label = model.predict(X)[0]
        proba = float(model.predict_proba(X).max())

        return jsonify({
            "previsao": LABEL_MAP.get(prediction_label, "Desconhecido"),
            "probabilidade": round(proba, 2)
        })

    except Exception as e:
        import traceback
        traceback.print_exc()
        return jsonify({"erro": str(e)}), 500



if __name__ == '__main__':
    app.run(host = "0.0.0.0", port=5000)

```

-----

### 2\. Time de Back-End (Java Spring Boot)

O Spring Boot será a "porta de entrada". Ele recebe a requisição do cliente, valida, chama o Python, e devolve a resposta.

**Estrutura do Projeto:**

  * `SentimentController.java`: Recebe o POST.
  * `SentimentService.java`: Lógica de negócio e chamada HTTP ao Python.
  * `SentimentRequest.java` e `SentimentResponse.java`: DTOs (Data Transfer Objects).

#### Dependências (`pom.xml`)

Adicione apenas `spring-boot-starter-web`.

#### A. Os DTOs (Classes de dados)

```java
// SentimentRequest.java
public class SentimentRequest {
    private String text;
    // Getters e Setters
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}

// SentimentResponse.java
public class SentimentResponse {
    private String previsao;
    private Double probabilidade;
    // Getters e Setters, Construtores
}
```

#### B. O Service (Lógica e Integração)

Aqui usamos `RestTemplate` para conectar o Java ao Python.

```java
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

@Service
public class SentimentService {

    // URL do serviço Python
    private final String ML_SERVICE_URL = "http://localhost:5000/predict";

    public SentimentResponse analisarSentimento(String texto) {
        // 1. Validação de Regra de Negócio
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("O texto não pode estar vazio.");
        }

        // 2. Preparar requisição para o Python
        RestTemplate restTemplate = new RestTemplate();
        SentimentRequest request = new SentimentRequest();
        request.setText(texto);

        // 3. Chamar o Python e pegar resposta
        try {
            SentimentResponse response = restTemplate.postForObject(ML_SERVICE_URL, request, SentimentResponse.class);
            return response;
        } catch (Exception e) {
            // Fallback caso o Python esteja fora do ar
            throw new RuntimeException("Erro ao conectar com o serviço de IA: " + e.getMessage());
        }
    }
}
```

#### C. O Controller (O Endpoint)

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sentiment")
public class SentimentController {

    @Autowired
    private SentimentService service;

    @PostMapping
    public ResponseEntity<?> classificar(@RequestBody SentimentRequest request) {
        try {
            SentimentResponse resultado = service.analisarSentimento(request.getText());
            return ResponseEntity.ok(resultado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro interno: " + e.getMessage());
        }
    }
}
```

-----

### 3\. Como Rodar e Testar (O Passo a Passo)

Para apresentar isso no Hackathon, siga esta ordem exata:

1.  **Prepare o Ambiente Python:**

    ```bash
    pip install pandas scikit-learn flask
    python treinar_modelo.py  # Isso vai criar o arquivo .joblib
    python app_python.py      # Isso sobe o servidor na porta 5000
    ```

2.  **Suba o Spring Boot:**

      * Execute a aplicação Java (geralmente porta 8080).

3.  **Teste (Via Postman ou cURL):**

**Requisição (POST para localhost:8080/sentiment):**

```json
{
  "text": "O atendimento foi péssimo, odiei tudo."
}
```

**Resposta Esperada:**

```json
{
  "previsao": "Negativo",
  "probabilidade": 0.85
}
```



}
=======
# hackathon_ONE
Hackathon One 2025-26 (Oracle + Alura)
>>>>>>> 56c105e46db9bb4b5a79bb5c97091fe38ea06e03
