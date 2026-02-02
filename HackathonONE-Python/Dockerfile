# Usamos uma imagem slim para ser mais leve
FROM python:3.9

WORKDIR /app

# Copia a lista de dependências primeiro (otimiza o cache)
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# Copia TUDO da pasta atual (incluindo o seu .py e o seu .pkl)
COPY . .

# Expõe a porta que o Flask está usando
EXPOSE 5000

# Comando para rodar
CMD ["python", "app_python.py"]
