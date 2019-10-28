# Sea-otter
JPEG and PNG images optimized application

- upload image file
- support edit command
  - coordinate crop, position crop, point with scale crop
  - resize, scale resize
  - angle rotate, flip rotate
- support image 
  - JPG, JPEG, TIFF, BMP, PNG, HDR
- support storage 
  - aws s3
  - local storage  
     
# New Features!
 - Upload the original image.
 - It supports json in the form of base64 safe url.
 
# Installation
### Quick start
```sh
git clone https://github.com/fennec-fox/sea-otter.git
./quick-start.sh
```
Swagger api page is http://localhost:6100/swagger-ui.html

### Configuration
```yaml
app:
  uploader: local
  local-storage:
    path: /var/tmp/image
    url: http://localhost
    shard-type: date
  aws-s3:
    bucket: sea-otter
    path: /edit
    cloud-front:
      url: http://d111111abcdef8.cloudfront.net
    shard-type: date
```
- app.uploader: `local`, `s3`
  - `local`: using local storage 
  - `s3`: using aws s3
- app.local-storage: local storage config
  - path: upload file directory
  - url: web host 
     - When a file is uploaded, it generates a url. Enter the host information of the url to be used. 
     - ex) when url is `http://localhost`, uploaded url is `http://localhost/2019-10-28/edited/5db6b0fc9216993dcfacde84.jpg`
  - shard-type: `date`, `sha1`
    - `date`: uri use date path
      - ex) `http://localhost/2019-10-28/edited/5db6b0fc9216993dcfacde84.jpg`
    - `sha1`: uri use encrypt string path  
      - ex) `http://localhost/5db6b0fc9216993dcfa2s3sf/edited/5db6b0fc9216993dcfacde84.jpg`
- app.aws-s3: amazon s3 config (If you use aws, you should configure aws certification on the server.)
  - bucket: s3 bucket name
  - path: bucket directory path
  - cloud-front: amazon cloud front config
    - url: amazon cloud front host
  - shard-type: `date`, `sha1`
    - `date`: uri use date path
      - ex) `http://d111111abcdef8.cloudfront.net/2019-10-28/edited/5db6b0fc9216993dcfacde84.jpg`
    - `sha1`: uri use encrypt string path  
      - ex) `http://d111111abcdef8.cloudfront.net/5db6b0fc9216993dcfa2s3sf/edited/5db6b0fc9216993dcfacde84.jpg`        
  
# Example
// TODO
