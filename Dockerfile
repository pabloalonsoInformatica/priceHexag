FROM sonarqube:10.7.0-community

USER root
RUN sysctl -w vm.max_map_count=262144
USER sonarqube
