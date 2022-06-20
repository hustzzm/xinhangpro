FROM 10.100.16.54/onepiece/nginx:1.19.0
ADD dist /root/product_react_essplus
ADD nginx.conf /etc/nginx/nginx.conf
RUN cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
EXPOSE 8080
ENTRYPOINT ["/usr/bin/env"]
CMD ["nginx","-c","/etc/nginx/nginx.conf"]
