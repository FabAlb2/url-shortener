# Dies ist meine Lösung der DevOps Challenge. EIn URL shortener - Springboot + Docker + Helm + CI/CD

## Part 1: Anwendung

### zum lokal starten
./gradlew bootRun



## Part 2: Docker

### Docker-image bauen
docker build -t url-shortener:latest .

### Container starten
docker run --rm -p 8080:8080 url-shortener:latest

### Das öffentliche Docker-image
https://hub.docker.com/r/fabalb2/url-shortener

### image-Name
fabalb2/url-shortener:0.1.0



## Part 3: Helm und Kubernetes

### Cluster starten
minikube start --driver=docker
kubectl get nodes

### Helm-Chart installieren
helm install url-shortener ./helm-chart

### Port Weiterleitung
kubectl port-forward svc/url-shortener 8080:80


## Part 4: CI/CD

Pull Request: automatische Unit Tests

### Workflow Datei
.github/workflows/ci.yml

