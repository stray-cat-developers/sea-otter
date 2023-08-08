# Sea-otter
JPEG and PNG images optimized application

[![Build Status](https://travis-ci.org/fennec-fox/sea-otter.svg?branch=master)](https://travis-ci.org/fennec-fox/sea-otter)

- upload image file
- support edit command
  - coordinate crop, position crop, point with scale crop
  - resize, scale resize
  - angle rotate, flip rotate
- support image 
  - JPG, JPEG, TIFF, BMP, PNG, HDR, WEBP
- support storage 
  - aws s3
  - local storage  
     
# New Features!
##### 0.2.1
- Support webP format
- Fix Azure Blob Uploader  
##### 0.1.4 
 - Upload using the image url
##### 0.1.3
 - Support azure client
##### 0.1.2
- Refactor test code
##### 0.1.1
 - Upgrade library
   - Springboot 2.1.3 to 2.2.6
   - kotlin 1.3.31 to 1.3.70
   - kotlin coroutines 1.3.0 to 1.3.5
   - gradle 4.8 to 6.3
   - etc
##### 0.1.0
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
- common response spec
  - width: image width
  - height: image height
  - path: image url 
  - format: image format
  - original: whether the uploaded image is original
### Simple upload
#### Multipart file upload
Request 
```sh
curl -X POST "http://localhost:6100/upload/simple/multipart" -H "accept: application/json" -H "Content-Type: multipart/form-data" -F "hasOriginal=false" -F "multiPartFile=@map-marker-green.png;type=image/png"
```

Response
```json
{
  "content": [
    {
      "width": 48,
      "height": 48,
      "path": "http://localhost/2019-10-29/edited/5db7a6eae8dc502049df1734.jpg",
      "format": "jpg",
      "original": false
    }
  ]
}
```

#### Base64 upload
Request 
```sh
curl -X POST "http://localhost:6100/upload/simple/base64/form" -H "accept: application/json" -H "Content-Type: application/x-www-form-urlencoded" -d "base64=data%3Aimage%2Fjpeg%3Bbase64%2C%2F9j%2F4AAQSkZJRgABAgAAAQABAAD%2F2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0aHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL%2F2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL%2FwAARCAABAAEDASIAAhEBAxEB%2F8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL%2F8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4%2BTl5ufo6erx8vP09fb3%2BPn6%2F8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL%2F8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3%2BPn6%2F9oADAMBAAIRAxEAPwDu6KKKxGf%2F2Q%3D%3D&hasOriginal=true"
```

Response
```json
{
  "content": [
    {
      "width": 1,
      "height": 1,
      "path": "http://localhost/2019-10-29/untouched/5db7e194e8dc502049df1739.jpeg",
      "format": "jpeg",
      "original": true
    },
    {
      "width": 1,
      "height": 1,
      "path": "http://localhost/2019-10-29/edited/5db7e194e8dc502049df1739.jpg",
      "format": "jpg",
      "original": false
    }
  ]
}
```
#### Base64 safe url upload by json
Due to json's parsing issue, we need to convert the image to base64 and then convert the base64 to a base64 safe url.
Request
```sh
curl -X POST "http://localhost:6100/upload/simple/base64/json" -H "accept: application/json" -H "Content-Type: application/json" -d "{ \"base64\": \"ZGF0YTppbWFnZS9qcGVnO2Jhc2U2NCwvOWovNEFBUVNrWkpSZ0FCQWdBQUFRQUJBQUQvMndCREFBZ0dCZ2NHQlFnSEJ3Y0pDUWdLREJRTkRBc0xEQmtTRXc4VUhSb2ZIaDBhSEJ3Z0pDNG5JQ0lzSXh3Y0tEY3BMREF4TkRRMEh5YzVQVGd5UEM0ek5ETC8yd0JEQVFrSkNRd0xEQmdORFJneUlSd2hNakl5TWpJeU1qSXlNakl5TWpJeU1qSXlNakl5TWpJeU1qSXlNakl5TWpJeU1qSXlNakl5TWpJeU1qSXlNakl5TWpML3dBQVJDQUFCQUFFREFTSUFBaEVCQXhFQi84UUFId0FBQVFVQkFRRUJBUUVBQUFBQUFBQUFBQUVDQXdRRkJnY0lDUW9MLzhRQXRSQUFBZ0VEQXdJRUF3VUZCQVFBQUFGOUFRSURBQVFSQlJJaE1VRUdFMUZoQnlKeEZES0JrYUVJSTBLeHdSVlMwZkFrTTJKeWdna0tGaGNZR1JvbEppY29LU28wTlRZM09EazZRMFJGUmtkSVNVcFRWRlZXVjFoWldtTmtaV1puYUdscWMzUjFkbmQ0ZVhxRGhJV0doNGlKaXBLVGxKV1dsNWlabXFLanBLV21wNmlwcXJLenRMVzJ0N2k1dXNMRHhNWEd4OGpKeXRMVDFOWFcxOWpaMnVIaTQrVGw1dWZvNmVyeDh2UDA5ZmIzK1BuNi84UUFId0VBQXdFQkFRRUJBUUVCQVFBQUFBQUFBQUVDQXdRRkJnY0lDUW9MLzhRQXRSRUFBZ0VDQkFRREJBY0ZCQVFBQVFKM0FBRUNBeEVFQlNFeEJoSkJVUWRoY1JNaU1vRUlGRUtSb2JIQkNTTXpVdkFWWW5MUkNoWWtOT0VsOFJjWUdSb21KeWdwS2pVMk56ZzVPa05FUlVaSFNFbEtVMVJWVmxkWVdWcGpaR1ZtWjJocGFuTjBkWFozZUhsNmdvT0VoWWFIaUltS2twT1VsWmFYbUptYW9xT2twYWFucUttcXNyTzB0YmEzdUxtNndzUEV4Y2JIeU1uSzB0UFUxZGJYMk5uYTR1UGs1ZWJuNk9ucTh2UDA5ZmIzK1BuNi85b0FEQU1CQUFJUkF4RUFQd0R1NktLS3hHZi8yUT09\", \"hasOriginal\": false}"
```

Response
```json
{
  "content": [
    {
      "width": 1,
      "height": 1,
      "path": "http://localhost/2019-10-29/edited/5db8142cd4ed6e32ac35fb05.jpg",
      "format": "jpg",
      "original": false
    }
  ]
}
```
### Image edit and upload
#### Editing Format 
- Operation name: **crop** 
  - Option name: *coordinate*
    - params
      - x1: X-axis of the first coordinate.
      - y1: Y-axis of the first coordinate.
      - x2: X-axis of the second coordinate.
      - y2: Y-axis of the second coordinate.
    - form example 
       ```
       1:crop=coordinate:100,100,400,400
       ```
    - json example 
       ```
        {
            "crop":{
               "coordinate":{
                  "x1":1,
                  "y1":2,
                  "x2":3,
                  "y2":4
               }
            }
         }               
       ```  
  - Option name: *position*
    - params
      - position: `CENTER`, `LEFT_UPPER` Anchor point of the crop
      - width: The width of the part to be crop
      - height: The height of the part to be crop      
    - form example 
       ```
       1:crop=position:CENTER,100,100
       ```
    - json example 
       ```
        {
            "crop":{
               "position":{
                  "position": "CENTER",
                  "width":100,
                  "height":100
               }
            }
         }                  
       ```
  - Option name: *pointScale*
    - params
      - x1: X-axis of reference coordinate.
      - y1: Y-axis of reference coordinate.
      - width: width 
      - height: height
    - form example 
       ```
       1:crop=pointScale:100,100,400,400
       ```       
    - json example 
       ```
        {
            "crop":{
               "pointScale":{
                  "x1": 100,
                  "y1":100,
                  "width": 100,
                  "height": 200
               }
            }
         }                   
       ```
- Operation name: **resize**
  - Option name: *size*
    - params
      - width: Edited image width
      - height: Edited image height
      - keepRatio: Fixing the ratio sets the ratio based on width.      
    - form example 
       ```
       1:resize=size:100,100,true
       ```
    - json example 
       ```
        {
            "resize":{
               "size":{                  
                  "width": 100,
                  "height": 200,
                  "keepRatio": "true"
               }
            }
         }                 
       ```   
- Option name: *scale*
    - params
      - scale: Resize the image on a percentage basis           
    - form example 
       ```
       1:resize=scale:50.0
       ```
    - json example 
       ```
        {
            "resize":{
               "scale":{                  
                  "scale": 50.0
               }
            }
         }                     
       ```   
- Operation name: **rotate**
  - Option name: *angle*
    - params
      - degree: Angle to rotate            
    - form example 
       ```
       1:rotate=angle:270.0
       ```
    - json example 
       ```
        {
            "rotate":{
               "angle":{                  
                  "degree": 270.0
               }
            }
         }                     
       ```   
- Option name: *flip*
    - params
      - flap: `HORZ`, `VERT` Flip the image up and down, left and right.           
    - form example 
       ```
       1:rotate=flip:HORZ
       ```
    - json example 
       ```
        {
            "rotate":{
               "flip":{                  
                  "flap": "HORZ"
               }
            }
         }                 
       ```   

#### Multipart file upload       
Request
```sh
curl -X POST "http://localhost:6100/upload/editing/multipart?1%3Acrop=coordinate%3A0%2C0%2C10%2C10&2%3Arotate=flip%3AHORZ" -H "accept: application/json" -H "Content-Type: multipart/form-data" -F "hasOriginal=false" -F "multiPartFile=@5db7e08ae8dc502049df1735.jpg;type=image/jpeg"
```
Response 
```json
{
  "content": [
    {
      "width": 10,
      "height": 10,
      "path": "http://localhost/2019-10-30/edited/5db93b22769d090d7fa62bfe.jpg",
      "format": "jpg",
      "original": false,
      "histories": [
        "crop",
        "rotate"
      ]
    }
  ]
}
```
#### Base64 upload
Request
```sh
curl -X POST "http://localhost:6100/upload/editing/base64/form?1%3Arotate=flip%3AHORZ" -H "accept: application/json" -H "Content-Type: application/x-www-form-urlencoded" -d "base64=data%3Aimage%2Fpng%3Bbase64%2CiVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5%2BAAAMS2lDQ1BJQ0MgUHJvZmlsZQAASImVVwdYU8kWnltSSWiBUKSE3kQp0qWE0CIISBVEJSSBhBJjQhCxsyyr4NpFBNQVXRVRdC2ArBV7WRS7a3koi8rKuliwofImBdZ1v%2Ffe9873zb1%2Fzpzzn5K5984AoFPHk0rzUV0ACiSFsoTIUNbktHQWqRsgAAVUQARMHl8uZcfHxwAow%2Fe%2Fy5ub0BrKNVcl1z%2Fn%2F6voCYRyPgBIPMRZAjm%2FAOIDAOBlfKmsEACiL9TbzCqUKnEGxAYymCDEUiXOUeMyJc5S42qVTVICB%2BJdAJBpPJ4sBwDtVqhnFfFzII%2F2bYjdJAKxBAAdMsRBfBFPAHEUxKMLCmYoMbQDjllf8OT8jTNrhJPHyxnB6lpUQg4Ty6X5vNn%2FZzv%2BtxTkK4Zj2MNBE8miEpQ1w77dzpsRrcQ0iPskWbFxEOtD%2FE4sUNlDjFJFiqhktT1qxpdzYM8AE2I3AS8sGmIziCMk%2BbExGn1WtjiCCzFcIWixuJCbpPFdLJSHJ2o462QzEuKGcbaMw9b4NvFkqrhK%2B1OKvGS2hv%2B2SMgd5n9dIkpKVeeMUYvEKbEQa0PMlOclRqttMNsSESd22EamSFDmbwuxv1ASGarmx6ZlyyISNPayAvlwvdhikZgbq8E1haKkKA3PLj5Plb8xxK1CCTt5mEconxwzXItAGBaurh27IpQka%2BrFuqSFoQka35fS%2FHiNPU4V5kcq9dYQm8mLEjW%2BeFAhXJBqfjxWWhifpM4Tz8rlTYhX54MXgxjAAWGABRRwZIEZIBeIO%2Fpa%2BuAv9UwE4AEZyAFC4KrRDHukqmYk8JoISsAfEAmBfMQvVDUrBEVQ%2F2lEq766gmzVbJHKIw88hrgARIN8%2BFuh8pKMREsBv0GN%2BB%2FR%2BTDXfDiUc%2F%2FUsaEmRqNRDPOydIYtieHEMGIUMYLohJviQXgAHgOvIXB44L6433C2f9kTHhM6CY8INwhdhDvTxaWyr%2BphgYmgC0aI0NSc9WXNuD1k9cJD8UDID7lxJm4KXPFxMBIbD4axvaCWo8lcWf3X3H%2Br4Yuua%2BwobhSUYkQJoTh%2B7antrO01wqLs6ZcdUueaNdJXzsjM1%2FE5X3RaAO%2FRX1tii7H92FnsBHYeO4y1ABZ2DGvFLmFHlHhkFf2mWkXD0RJU%2BeRBHvE%2F4vE0MZWdlLs1uvW6fVTPFQqLle9HwJkhnS0T54gKWWz45heyuBL%2BmNEsDzd3PwCU3xH1a%2BoVU%2FV9QJgX%2FtKVvgYgUDA0NHT4L10MfKYPfAsA9fFfOoej8HVgBMC5Sr5CVqTW4coLAX6ddOATZQIsgA1whPV4AG8QAEJAOJgA4kASSAPTYJdFcD3LwCwwFywC5aASrABrQQ3YBLaAHWA32AdawGFwApwBF8EVcAPchaunBzwD%2FeANGEQQhITQEQZiglgidogL4oH4IkFIOBKDJCBpSCaSg0gQBTIX%2BQapRFYhNchmpAH5CTmEnEDOI53IHeQh0ou8RD6gGEpDDVBz1B4di%2FqibDQaTUKnojnoTLQELUOXodVoPboLbUZPoBfRG2gX%2BgwdwACmhTExK8wV88U4WByWjmVjMmw%2BVoFVYfVYE9YG%2F%2BdrWBfWh73HiTgDZ%2BGucAVH4ck4H5%2BJz8eX4jX4DrwZP4Vfwx%2Fi%2FfhnAp1gRnAh%2BBO4hMmEHMIsQjmhirCNcJBwGj5NPYQ3RCKRSXQg%2BsCnMY2YS5xDXErcQNxDPE7sJHYTB0gkkgnJhRRIiiPxSIWkctJ60i7SMdJVUg%2FpHVmLbEn2IEeQ08kScim5iryTfJR8lfyEPEjRpdhR%2FClxFAFlNmU5ZSuljXKZ0kMZpOpRHaiB1CRqLnURtZraRD1NvUd9paWlZa3lpzVJS6y1UKtaa6%2FWOa2HWu9p%2BjRnGoeWQVPQltG2047T7tBe0el0e3oIPZ1eSF9Gb6CfpD%2Bgv9NmaI%2FR5moLtBdo12o3a1%2FVfq5D0bHTYetM0ynRqdLZr3NZp0%2BXomuvy9Hl6c7XrdU9pHtLd0CPoeeuF6dXoLdUb6feeb2n%2BiR9e%2F1wfYF%2Bmf4W%2FZP63QyMYcPgMPiMbxhbGacZPQZEAwcDrkGuQaXBboMOg35DfcNxhimGxYa1hkcMu5gY057JZeYzlzP3MW8yPxiZG7GNhEZLjJqMrhq9NR5lHGIsNK4w3mN8w%2FiDCcsk3CTPZKVJi8l9U9zU2XSS6SzTjaanTftGGYwKGMUfVTFq36hfzVAzZ7MEszlmW8wumQ2YW5hHmkvN15ufNO%2BzYFqEWORarLE4atFrybAMshRbrrE8Zvk7y5DFZuWzqlmnWP1WZlZRVgqrzVYdVoPWDtbJ1qXWe6zv21BtfG2ybdbYtNv021raTrSda9to%2B6sdxc7XTmS3zu6s3Vt7B%2FtU%2B%2B%2FsW%2ByfOhg7cB1KHBod7jnSHYMdZzrWO153Ijr5OuU5bXC64ow6ezmLnGudL7ugLt4uYpcNLp2jCaP9RktG14%2B%2B5UpzZbsWuTa6PhzDHBMzpnRMy5jnY23Hpo9dOfbs2M9uXm75blvd7rrru09wL3Vvc3%2Fp4ezB96j1uO5J94zwXODZ6vlinMs44biN4257Mbwmen3n1e71ydvHW%2Bbd5N3rY%2BuT6VPnc8vXwDfed6nvOT%2BCX6jfAr%2FDfu%2F9vf0L%2Fff5%2FxngGpAXsDPg6XiH8cLxW8d3B1oH8gI3B3YFsYIyg34I6gq2CuYF1wc%2FCrEJEYRsC3nCdmLnsnexn4e6hcpCD4a%2B5fhz5nGOh2FhkWEVYR3h%2BuHJ4TXhDyKsI3IiGiP6I70i50QejyJERUetjLrFNefyuQ3c%2Fgk%2BE%2BZNOBVNi06Mrol%2BFOMcI4tpm4hOnDBx9cR7sXaxktiWOBDHjVsddz%2FeIX5m%2FM%2BTiJPiJ9VOepzgnjA34WwiI3F64s7EN0mhScuT7iY7JiuS21N0UjJSGlLepoalrkrtmjx28rzJF9NM08Rpremk9JT0bekDU8KnrJ3Sk%2BGVUZ5xc6rD1OKp56eZTsufdmS6znTe9P2ZhMzUzJ2ZH3lxvHreQBY3qy6rn8%2Fhr%2BM%2FE4QI1gh6hYHCVcIn2YHZq7Kf5gTmrM7pFQWLqkR9Yo64RvwiNyp3U%2B7bvLi87XlD%2Ban5ewrIBZkFhyT6kjzJqRkWM4pndEpdpOXSrpn%2BM9fO7JdFy7bJEflUeWuhAdywX1I4Kr5VPCwKKqotejcrZdb%2BYr1iSfGl2c6zl8x%2BUhJR8uMcfA5%2FTvtcq7mL5j6cx563eT4yP2t%2B%2BwKbBWULehZGLtyxiLoob9EvpW6lq0pff5P6TVuZednCsu5vI79tLNcul5Xf%2Bi7gu02L8cXixR1LPJesX%2FK5QlBxodKtsqry41L%2B0gvfu39f%2Ff3QsuxlHcu9l29cQVwhWXFzZfDKHav0VpWs6l49cXXzGtaaijWv105fe75qXNWmddR1inVd1THVrett169Y%2F7FGVHOjNrR2T51Z3ZK6txsEG65uDNnYtMl8U%2BWmDz%2BIf7i9OXJzc719fdUW4paiLY%2B3pmw9%2B6Pvjw3bTLdVbvu0XbK9a0fCjlMNPg0NO812Lm9EGxWNvbsydl3ZHba7tcm1afMe5p7KvWCvYu%2FvP2X%2BdHNf9L72%2Fb77mw7YHag7yDhY0Yw0z27ubxG1dLWmtXYemnCovS2g7eDPY37eftjqcO0RwyPLj1KPlh0dOlZybOC49HjfiZwT3e3T2%2B%2BenHzy%2BqlJpzpOR58%2BdybizMmz7LPHzgWeO3ze%2F%2FyhC74XWi56X2y%2B5HXp4C9evxzs8O5ovuxzufWK35W2zvGdR68GXz1xLezamevc6xdvxN7ovJl88%2FatjFtdtwW3n97Jv%2FPi16JfB%2B8uvEe4V3Ff937VA7MH9f9y%2BteeLu%2BuIw%2FDHl56lPjobje%2F%2B9lv8t8%2B9pQ9pj%2BuemL5pOGpx9PDvRG9V36f8nvPM%2Bmzwb7yP%2FT%2BqHvu%2BPzAnyF%2FXuqf3N%2FzQvZi6OXSVyavtr8e97p9IH7gwZuCN4NvK96ZvNvx3vf92Q%2BpH54MzvpI%2Blj9yelT2%2Bfoz%2FeGCoaGpDwZT7UVwOBAs7MBeLkdAHoaAIwrcP8wRX3OUwmiPpuqEPhPWH0WVIk3AE3wptyuc44DsBcO%2B4WQOwQA5VY9KQSgnp4jQyPybE8PNRcNnngI74aGXpkDQGoD4JNsaGhww9DQp60w2TsAHJ%2BpPl8qhQjPBj%2BEKNEN44x34Cv5Nwr4f7YV%2BwhqAAAACXBIWXMAABYlAAAWJQFJUiTwAAABmWlUWHRYTUw6Y29tLmFkb2JlLnhtcAAAAAAAPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iWE1QIENvcmUgNS40LjAiPgogICA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPgogICAgICA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIgogICAgICAgICAgICB4bWxuczpleGlmPSJodHRwOi8vbnMuYWRvYmUuY29tL2V4aWYvMS4wLyI%2BCiAgICAgICAgIDxleGlmOlBpeGVsWERpbWVuc2lvbj40PC9leGlmOlBpeGVsWERpbWVuc2lvbj4KICAgICAgICAgPGV4aWY6UGl4ZWxZRGltZW5zaW9uPjQ8L2V4aWY6UGl4ZWxZRGltZW5zaW9uPgogICAgICA8L3JkZjpEZXNjcmlwdGlvbj4KICAgPC9yZGY6UkRGPgo8L3g6eG1wbWV0YT4KlzPb1AAAABxpRE9UAAAAAgAAAAAAAAACAAAAKAAAAAIAAAACAAAAV04ImGMAAAAjSURBVBgZYnx4995%2FRkZGhv%2F%2F%2FzOAaMZH9%2B4DBRiAAgxgAAAAAP%2F%2F5V%2FOuQAAACJJREFUY3x8%2F%2F7%2F%2F%2F8ZGBgZGRjA9JP7D%2F7%2F%2B%2F8XKMAMFgAAzkEcfl0bzSUAAAAASUVORK5CYII%3D&hasOriginal=false"
```
Response 
```json
{
  "content": [
    {
      "width": 4,
      "height": 4,
      "path": "http://localhost/2019-10-30/edited/5db93dd0ba193f2d860430d8.jpg",
      "format": "jpg",
      "original": false,
      "histories": [
        "rotate"
      ]
    }
  ]
}
```
#### Base64 safe url upload by json
Due to json's parsing issue, we need to convert the image to base64 and then convert the base64 to a base64 safe url.
Request Sample
```json
{
  "edits": [
    {
      "crop": {
        "coordinate": {
          "x1": 0,
          "y1": 0,
          "x2": 1,
          "y2": 1
        }
      },
      "rotate":{
        "angle":{                  
          "degree": 270.0
        }
      }      
    }
  ],
  "base64": "ZGF0YTppbWFnZS9wbmc7YmFzZTY0LGlWQk9SdzBLR2dvQUFBQU5TVWhFVWdBQUFBUUFBQUFFQ0FZQUFBQ3A4WjUrQUFBTVMybERRMUJKUTBNZ1VISnZabWxzWlFBQVNJbVZWd2RZVThrV25sdFNTV2lCVUtTRTNrUXAwcVdFMENJSVNCVkVKU1NCaEJKalFoQ3hzeXlyNE5wRkJOUVZYUlZSZEMyQXJCVjdXUlM3YTNrb2k4ckt1bGl3b2ZJbUJkWjF2L2ZlOTg3M3piMS96cHp6bjVLNTk4NEFvRlBIazByelVWMEFDaVNGc29USVVOYmt0SFFXcVJzZ0FBVlVRQVJNSGw4dVpjZkh4d0Fvdy9lL3k1dWIwQnJLTlZjbDF6L24vNnZvQ1lSeVBnQklQTVJaQWptL0FPSURBT0JsZkttc0VBQ2lMOVRiekNxVUtuRUd4QVl5bUNERVVpWE9VZU15SmM1UzQycVZUVklDQitKZEFKQnBQSjRzQndEdFZxaG5GZkZ6SUkvMmJZamRKQUt4QkFBZE1zUkJmQkZQQUhFVXhLTUxDbVlvTWJRRGpsbGY4T1Q4alROcmhKUEh5eG5CNmxwVVFnNFR5Nlg1dk5uL1p6dit0eFRrSzRaajJNTkJFOG1pRXBRMXc3N2R6cHNScmNRMGlQc2tXYkZ4RU90RC9FNHNVTmxEakZKRmlxaGt0VDFxeHBkellNOEFFMkkzQVM4c0dtSXppQ01rK2JFeEduMVd0amlDQ3pGY0lXaXh1SkNicFBGZExKU0hKMm80NjJRekV1S0djYmFNdzliNE52RmtxcmhLKzFPS3ZHUzJodisyU01nZDVuOWRJa3BLVmVlTVVZdkVLYkVRYTBQTWxPY2xScXR0TU5zU0VTZDIyRWFtU0ZEbWJ3dXh2MUFTR2FybXg2Wmx5eUlTTlBheUF2bHd2ZGhpa1pnYnE4RTFoYUtrS0EzUExqNVBsYjh4eEsxQ0NUdDVtRWNvbnh3elhJdEFHQmF1cmgyN0lwUWthK3JGdXFTRm9Ra2EzNWZTL0hpTlBVNFY1a2NxOWRZUW04bUxFalcrZUZBaFhKQnFmanhXV2hpZnBNNFR6OHJsVFloWDU0TVhneGpBQVdHQUJSUndaSUVaSUJlSU8vcGErdUF2OVV3RTRBRVp5QUZDNEtyUkRIdWtxbVlrOEpvSVNzQWZFQW1CZk1RdlZEVXJCRVZRLzJsRXE3NjZnbXpWYkpIS0l3ODhocmdBUklOOCtGdWg4cEtNUkVzQnYwR04rQi9SK1REWGZEaVVjLy9Vc2FFbVJxTlJEUE95ZElZdGllSEVNR0lVTVlMb2hKdmlRWGdBSGdPdklYQjQ0TDY0MzNDMmY5a1RIaE02Q1k4SU53aGRoRHZUeGFXeXIrcGhnWW1nQzBhSTBOU2M5V1hOdUQxazljSkQ4VURJRDdseEptNEtYUEZ4TUJJYkQ0YXh2YUNXbzhsY1dmM1gzSCtyNFl1dWErd29iaFNVWWtRSm9UaCs3YW50ck8wMXdxTHM2WmNkVXVlYU5kSlh6c2pNMS9FNVgzUmFBTy9SWDF0aWk3SDkyRm5zQkhZZU80eTFBQloyREd2RkxtRkhsSGhrRmYybVdrWEQwUkpVK2VSQkh2RS80dkUwTVpXZGxMczF1dlc2ZlZUUEZRcUxsZTlId0praG5TMFQ1NGdLV1d6NDVoZXl1QkwrbU5Fc0R6ZDNQd0NVM3hIMWErb1ZVL1Y5UUpnWC90S1Z2Z1lnVURBME5IVDRMMTBNZktZUGZBc0E5ZkZmT29lajhIVmdCTUM1U3I1Q1ZxVFc0Y29MQVg2ZGRPQVRaUUlzZ0Exd2hQVjRBRzhRQUVKQU9KZ0E0a0FTU0FQVFlKZEZjRDNMd0N3d0Z5d0M1YUFTckFCclFRM1lCTGFBSFdBMzJBZGF3R0Z3QXB3QkY4RVZjQVBjaGF1bkJ6d0QvZUFOR0VRUWhJVFFFUVppZ2xnaWRvZ0w0b0g0SWtGSU9CS0RKQ0JwU0NhU2cwZ1FCVElYK1FhcFJGWWhOY2htcEFINUNUbUVuRURPSTUzSUhlUWgwb3U4UkQ2Z0dFcEREVkJ6MUI0ZGkvcWliRFFhVFVLbm9qbm9UTFFFTFVPWG9kVm9QYm9MYlVaUG9CZlJHMmdYK2d3ZHdBQ21oVEV4Szh3Vjg4VTRXQnlXam1Wak1tdytWb0ZWWWZWWUU5WUcvK2RyV0JmV2g3M0hpVGdEWitHdWNBVkg0Y2s0SDUrSno4ZVg0alg0RHJ3WlA0VmZ3eC9pL2ZobkFwMWdSbkFoK0JPNGhNbUVITUlzUWptaGlyQ05jSkJ3R2o1TlBZUTNSQ0tSU1hRZytzQ25NWTJZUzV4RFhFcmNRTnhEUEU3c0pIWVRCMGdra2duSmhSUklpaVB4U0lXa2N0SjYwaTdTTWRKVlVnL3BIVm1MYkVuMklFZVEwOGtTY2ltNWlyeVRmSlI4bGZ5RVBFalJwZGhSL0NseEZBRmxObVU1WlN1bGpYS1owa01acE9wUkhhaUIxQ1JxTG5VUnRacmFSRDFOdlVkOXBhV2xaYTNscHpWSlM2eTFVS3RhYTYvV09hMkhXdTlwK2pSbkdvZVdRVlBRbHRHMjA0N1Q3dEJlMGVsMGUzb0lQWjFlU0Y5R2I2Q2ZwRCtndjlObWFJL1I1bW9MdEJkbzEybzNhMS9WZnE1RDBiSFRZZXRNMHluUnFkTFpyM05acDArWG9tdXZ5OUhsNmM3WHJkVTlwSHRMZDBDUG9lZXVGNmRYb0xkVWI2ZmVlYjJuK2lSOWUvMXdmWUYrbWY0Vy9aUDYzUXlNWWNQZ01QaU1ieGhiR2FjWlBRWkVBd2NEcmtHdVFhWEJib01PZzM1RGZjTnhoaW1HeFlhMWhrY011NWdZMDU3SlplWXpselAzTVc4eVB4aVpHN0dOaEVaTGpKcU1yaHE5TlI1bEhHSXNOSzR3M21OOHcvaURDY3NrM0NUUFpLVkppOGw5VTl6VTJYU1M2U3pUamFhblRmdEdHWXdLR01VZlZURnEzNmhmelZBelo3TUVzemxtVzh3dW1RMllXNWhIbWt2TjE1dWZOTyt6WUZxRVdPUmFyTEU0YXRGcnliQU1zaFJicnJFOFp2azd5NURGWnVXenFsbW5XUDFXWmxaUlZncXJ6VllkVm9QV0R0YkoxcVhXZTZ6djIxQnRmRzJ5YmRiWXROdjAyMXJhVHJTZGE5dG8rNnNkeGM3WFRtUzN6dTZzM1Z0N0IvdFUrKy9zVyt5Zk9oZzdjQjFLSEJvZDdqblNIWU1kWnpyV08xNTNJanI1T3VVNWJYQzY0b3c2ZXptTG5HdWRMN3VnTHQ0dVlwY05McDJqQ2FQOVJrdEcxNCsrNVVwelpic1d1VGE2UGh6REhCTXpwblJNeTVqblkyM0hwbzlkT2ZiczJNOXVYbTc1Ymx2ZDdycnJ1MDl3TDNWdmMzL3A0ZXpCOTZqMXVPNUo5NHp3WE9EWjZ2bGluTXM0NGJpTjQyNTdNYndtZW4zbjFlNzF5ZHZIVytiZDVOM3JZK3VUNlZQbmM4dlh3RGZlZDZudk9UK0NYNmpmQXIvRGZ1Lzl2ZjBML2ZmNS94bmdHcEFYc0RQZzZYaUg4Y0x4VzhkM0Ixb0g4Z0kzQjNZRnNZSXlnMzRJNmdxMkN1WUYxd2MvQ3JFSkVZUnNDM25DZG1MbnNuZXhuNGU2aGNwQ0Q0YSs1Zmh6NW5HT2gyRmhrV0VWWVIzaCt1SEo0VFhoRHlLc0kzSWlHaVA2STcwaTUwUWVqeUpFUlVldGpMckZOZWZ5dVEzYy9naytFK1pOT0JWTmkwNk1yb2wrRk9NY0k0dHBtNGhPbkRCeDljUjdzWGF4a3RpV09CREhqVnNkZHovZUlYNW0vTStUaUpQaUo5Vk9lcHpnbmpBMzRXd2lJM0Y2NHM3RU4wbWhTY3VUN2lZN0ppdVMyMU4wVWpKU0dsTGVwb2FscmtydG1qeDI4cnpKRjlOTTA4UnByZW1rOUpUMGJla0RVOEtuckozU2srR1ZVWjV4YzZyRDFPS3A1NmVaVHN1ZmRtUzZ6blRlOVAyWmhNelV6SjJaSDNseHZIcmVRQlkzcXk2cm44L2hyK00vRTRRSTFnaDZoWUhDVmNJbjJZSFpxN0tmNWdUbXJNN3BGUVdMcWtSOVlvNjRSdndpTnlwM1UrN2J2TGk4N1hsRCthbjVld3JJQlprRmh5VDZranpKcVJrV000cG5kRXBkcE9YU3JwbitNOWZPN0pkRnk3YkpFZmxVZVd1aEFkeXdYMUk0S3I1VlBDd0tLcW90ZWpjclpkYitZcjFpU2ZHbDJjNnpsOHgrVWhKUjh1TWNmQTUvVHZ0Y3E3bUw1ajZjeDU2M2VUNHlQMnQrK3dLYkJXVUxlaFpHTHR5eGlMb29iOUV2cFc2bHEwcGZmNVA2VFZ1WmVkbkNzdTV2STc5dExOY3VsNVhmK2k3Z3UwMkw4Y1hpeFIxTFBKZXNYL0s1UWxCeG9kS3RzcXJ5NDFMKzBndmZ1MzlmL2YzUXN1eGxIY3U5bDI5Y1FWd2hXWEZ6WmZES0hhdjBWcFdzNmw0OWNYWHpHdGFhaWpXdjEwNWZlNzVxWE5XbWRkUjFpblZkMVRIVnJldHQxNjlZLzdGR1ZIT2pOclIyVDUxWjNaSzZ0eHNFRzY1dURObll0TWw4VStXbUR6K0lmN2k5T1hKemM3MTlmZFVXNHBhaUxZKzNwbXc5KzZQdmp3M2JUTGRWYnZ1MFhiSzlhMGZDamxNTlBnME5PODEyTG05RUd4V052YnN5ZGwzWkhiYTd0Y20xYWZNZTVwN0t2V0N2WXUvdlAyWCtkSE5mOUw3Mi9iNzdtdzdZSGFnN3lEaFkwWXcwejI3dWJ4RzFkTFdtdFhZZW1uQ292UzJnN2VEUFkzN2VmdGpxY08wUnd5UExqMUtQbGgwZE9sWnliT0M0OUhqZmlad1QzZTNUMisrZW5IenkrcWxKcHpwT1I1OCtkeWJpek1tejdMUEh6Z1dlTzN6ZS8veWhDNzRYV2k1NlgyeSs1SFhwNEM5ZXZ4enM4TzVvdnV4enVmV0szNVcyenZHZFI2OEdYejF4TGV6YW1ldmM2eGR2eE43b3ZKbDg4L2F0akZ0ZHR3VzNuOTdKdi9QaTE2SmZCKzh1dkVlNFYzRmY5MzdWQTdNSDlmOXkrdGVlTHUrdUl3L0RIbDU2bFBqb2JqZS8rOWx2OHQ4KzlwUTlwait1ZW1MNXBPR3B4OVBEdlJHOVYzNmY4bnZQTSttendiN3lQL1QrcUh2dStQekFueUYvWHVxZjNOL3pRdlppNk9YU1Z5YXZ0cjhlOTdwOUlIN2d3WnVDTjROdks5Nlp2TnZ4M3ZmOTJRK3BINTRNenZwSStsajl5ZWxUMitmb3ovZUdDb2FHcER3WlQ3VVZ3T0JBczdNQmVMa2RBSG9hQUl3cmNQOHdSWDNPVXdtaVBwdXFFUGhQV0gwV1ZJazNBRTN3cHR5dWM0NERzQmNPKzRXUU93UUE1Vlk5S1FTZ25wNGpReVB5YkU4UE5SY05ubmdJNzRhR1hwa0RRR29ENEpOc2FHaHd3OURRcDYwdzJUc0FISitwUGw4cWhRalBCaitFS05FTjQ0eDM0Q3Y1TndyNGY3WVYrd2hxQUFBQUNYQklXWE1BQUJZbEFBQVdKUUZKVWlUd0FBQUJtV2xVV0hSWVRVdzZZMjl0TG1Ga2IySmxMbmh0Y0FBQUFBQUFQSGc2ZUcxd2JXVjBZU0I0Yld4dWN6cDRQU0poWkc5aVpUcHVjenB0WlhSaEx5SWdlRHA0YlhCMGF6MGlXRTFRSUVOdmNtVWdOUzQwTGpBaVBnb2dJQ0E4Y21SbU9sSkVSaUI0Yld4dWN6cHlaR1k5SW1oMGRIQTZMeTkzZDNjdWR6TXViM0puTHpFNU9Ua3ZNREl2TWpJdGNtUm1MWE41Ym5SaGVDMXVjeU1pUGdvZ0lDQWdJQ0E4Y21SbU9rUmxjMk55YVhCMGFXOXVJSEprWmpwaFltOTFkRDBpSWdvZ0lDQWdJQ0FnSUNBZ0lDQjRiV3h1Y3pwbGVHbG1QU0pvZEhSd09pOHZibk11WVdSdlltVXVZMjl0TDJWNGFXWXZNUzR3THlJK0NpQWdJQ0FnSUNBZ0lEeGxlR2xtT2xCcGVHVnNXRVJwYldWdWMybHZiajQwUEM5bGVHbG1PbEJwZUdWc1dFUnBiV1Z1YzJsdmJqNEtJQ0FnSUNBZ0lDQWdQR1Y0YVdZNlVHbDRaV3haUkdsdFpXNXphVzl1UGpROEwyVjRhV1k2VUdsNFpXeFpSR2x0Wlc1emFXOXVQZ29nSUNBZ0lDQThMM0prWmpwRVpYTmpjbWx3ZEdsdmJqNEtJQ0FnUEM5eVpHWTZVa1JHUGdvOEwzZzZlRzF3YldWMFlUNEtselBiMUFBQUFCeHBSRTlVQUFBQUFnQUFBQUFBQUFBQ0FBQUFLQUFBQUFJQUFBQUNBQUFBVjA0SW1HTUFBQUFqU1VSQlZCZ1pZbng0OTk1L1JrWkdodi8vL3pPQWFNWkg5KzREQlJpQUFneGdBQUFBQVAvLzVWL091UUFBQUNKSlJFRlVZM3g4Ly83Ly8vOFpHQmdaR1JqQTlKUDdELzcvKy84WEtNQU1GZ0FBemtFY2ZsMGJ6U1VBQUFBQVNVVk9SSzVDWUlJPQ",
  "hasOriginal": false 
}
```
Request
```sh
curl -X POST "http://localhost:6100/upload/editing/base64/json" -H "accept: application/json" -H "Content-Type: application/json" -d "{ \"edits\": [ { \"crop\": { \"coordinate\": { \"x1\": 0, \"y1\": 0, \"x2\": 1, \"y2\": 1 } }, \"rotate\":{ \"angle\":{ \"degree\": 270.0 } } } ], \"base64\": \"ZGF0YTppbWFnZS9wbmc7YmFzZTY0LGlWQk9SdzBLR2dvQUFBQU5TVWhFVWdBQUFBUUFBQUFFQ0FZQUFBQ3A4WjUrQUFBTVMybERRMUJKUTBNZ1VISnZabWxzWlFBQVNJbVZWd2RZVThrV25sdFNTV2lCVUtTRTNrUXAwcVdFMENJSVNCVkVKU1NCaEJKalFoQ3hzeXlyNE5wRkJOUVZYUlZSZEMyQXJCVjdXUlM3YTNrb2k4ckt1bGl3b2ZJbUJkWjF2L2ZlOTg3M3piMS96cHp6bjVLNTk4NEFvRlBIazByelVWMEFDaVNGc29USVVOYmt0SFFXcVJzZ0FBVlVRQVJNSGw4dVpjZkh4d0Fvdy9lL3k1dWIwQnJLTlZjbDF6L24vNnZvQ1lSeVBnQklQTVJaQWptL0FPSURBT0JsZkttc0VBQ2lMOVRiekNxVUtuRUd4QVl5bUNERVVpWE9VZU15SmM1UzQycVZUVklDQitKZEFKQnBQSjRzQndEdFZxaG5GZkZ6SUkvMmJZamRKQUt4QkFBZE1zUkJmQkZQQUhFVXhLTUxDbVlvTWJRRGpsbGY4T1Q4alROcmhKUEh5eG5CNmxwVVFnNFR5Nlg1dk5uL1p6dit0eFRrSzRaajJNTkJFOG1pRXBRMXc3N2R6cHNScmNRMGlQc2tXYkZ4RU90RC9FNHNVTmxEakZKRmlxaGt0VDFxeHBkellNOEFFMkkzQVM4c0dtSXppQ01rK2JFeEduMVd0amlDQ3pGY0lXaXh1SkNicFBGZExKU0hKMm80NjJRekV1S0djYmFNdzliNE52RmtxcmhLKzFPS3ZHUzJodisyU01nZDVuOWRJa3BLVmVlTVVZdkVLYkVRYTBQTWxPY2xScXR0TU5zU0VTZDIyRWFtU0ZEbWJ3dXh2MUFTR2FybXg2Wmx5eUlTTlBheUF2bHd2ZGhpa1pnYnE4RTFoYUtrS0EzUExqNVBsYjh4eEsxQ0NUdDVtRWNvbnh3elhJdEFHQmF1cmgyN0lwUWthK3JGdXFTRm9Ra2EzNWZTL0hpTlBVNFY1a2NxOWRZUW04bUxFalcrZUZBaFhKQnFmanhXV2hpZnBNNFR6OHJsVFloWDU0TVhneGpBQVdHQUJSUndaSUVaSUJlSU8vcGErdUF2OVV3RTRBRVp5QUZDNEtyUkRIdWtxbVlrOEpvSVNzQWZFQW1CZk1RdlZEVXJCRVZRLzJsRXE3NjZnbXpWYkpIS0l3ODhocmdBUklOOCtGdWg4cEtNUkVzQnYwR04rQi9SK1REWGZEaVVjLy9Vc2FFbVJxTlJEUE95ZElZdGllSEVNR0lVTVlMb2hKdmlRWGdBSGdPdklYQjQ0TDY0MzNDMmY5a1RIaE02Q1k4SU53aGRoRHZUeGFXeXIrcGhnWW1nQzBhSTBOU2M5V1hOdUQxazljSkQ4VURJRDdseEptNEtYUEZ4TUJJYkQ0YXh2YUNXbzhsY1dmM1gzSCtyNFl1dWErd29iaFNVWWtRSm9UaCs3YW50ck8wMXdxTHM2WmNkVXVlYU5kSlh6c2pNMS9FNVgzUmFBTy9SWDF0aWk3SDkyRm5zQkhZZU80eTFBQloyREd2RkxtRkhsSGhrRmYybVdrWEQwUkpVK2VSQkh2RS80dkUwTVpXZGxMczF1dlc2ZlZUUEZRcUxsZTlId0praG5TMFQ1NGdLV1d6NDVoZXl1QkwrbU5Fc0R6ZDNQd0NVM3hIMWErb1ZVL1Y5UUpnWC90S1Z2Z1lnVURBME5IVDRMMTBNZktZUGZBc0E5ZkZmT29lajhIVmdCTUM1U3I1Q1ZxVFc0Y29MQVg2ZGRPQVRaUUlzZ0Exd2hQVjRBRzhRQUVKQU9KZ0E0a0FTU0FQVFlKZEZjRDNMd0N3d0Z5d0M1YUFTckFCclFRM1lCTGFBSFdBMzJBZGF3R0Z3QXB3QkY4RVZjQVBjaGF1bkJ6d0QvZUFOR0VRUWhJVFFFUVppZ2xnaWRvZ0w0b0g0SWtGSU9CS0RKQ0JwU0NhU2cwZ1FCVElYK1FhcFJGWWhOY2htcEFINUNUbUVuRURPSTUzSUhlUWgwb3U4UkQ2Z0dFcEREVkJ6MUI0ZGkvcWliRFFhVFVLbm9qbm9UTFFFTFVPWG9kVm9QYm9MYlVaUG9CZlJHMmdYK2d3ZHdBQ21oVEV4Szh3Vjg4VTRXQnlXam1Wak1tdytWb0ZWWWZWWUU5WUcvK2RyV0JmV2g3M0hpVGdEWitHdWNBVkg0Y2s0SDUrSno4ZVg0alg0RHJ3WlA0VmZ3eC9pL2ZobkFwMWdSbkFoK0JPNGhNbUVITUlzUWptaGlyQ05jSkJ3R2o1TlBZUTNSQ0tSU1hRZytzQ25NWTJZUzV4RFhFcmNRTnhEUEU3c0pIWVRCMGdra2duSmhSUklpaVB4U0lXa2N0SjYwaTdTTWRKVlVnL3BIVm1MYkVuMklFZVEwOGtTY2ltNWlyeVRmSlI4bGZ5RVBFalJwZGhSL0NseEZBRmxObVU1WlN1bGpYS1owa01acE9wUkhhaUIxQ1JxTG5VUnRacmFSRDFOdlVkOXBhV2xaYTNscHpWSlM2eTFVS3RhYTYvV09hMkhXdTlwK2pSbkdvZVdRVlBRbHRHMjA0N1Q3dEJlMGVsMGUzb0lQWjFlU0Y5R2I2Q2ZwRCtndjlObWFJL1I1bW9MdEJkbzEybzNhMS9WZnE1RDBiSFRZZXRNMHluUnFkTFpyM05acDArWG9tdXZ5OUhsNmM3WHJkVTlwSHRMZDBDUG9lZXVGNmRYb0xkVWI2ZmVlYjJuK2lSOWUvMXdmWUYrbWY0Vy9aUDYzUXlNWWNQZ01QaU1ieGhiR2FjWlBRWkVBd2NEcmtHdVFhWEJib01PZzM1RGZjTnhoaW1HeFlhMWhrY011NWdZMDU3SlplWXpselAzTVc4eVB4aVpHN0dOaEVaTGpKcU1yaHE5TlI1bEhHSXNOSzR3M21OOHcvaURDY3NrM0NUUFpLVkppOGw5VTl6VTJYU1M2U3pUamFhblRmdEdHWXdLR01VZlZURnEzNmhmelZBelo3TUVzemxtVzh3dW1RMllXNWhIbWt2TjE1dWZOTyt6WUZxRVdPUmFyTEU0YXRGcnliQU1zaFJicnJFOFp2azd5NURGWnVXenFsbW5XUDFXWmxaUlZncXJ6VllkVm9QV0R0YkoxcVhXZTZ6djIxQnRmRzJ5YmRiWXROdjAyMXJhVHJTZGE5dG8rNnNkeGM3WFRtUzN6dTZzM1Z0N0IvdFUrKy9zVyt5Zk9oZzdjQjFLSEJvZDdqblNIWU1kWnpyV08xNTNJanI1T3VVNWJYQzY0b3c2ZXptTG5HdWRMN3VnTHQ0dVlwY05McDJqQ2FQOVJrdEcxNCsrNVVwelpic1d1VGE2UGh6REhCTXpwblJNeTVqblkyM0hwbzlkT2ZiczJNOXVYbTc1Ymx2ZDdycnJ1MDl3TDNWdmMzL3A0ZXpCOTZqMXVPNUo5NHp3WE9EWjZ2bGluTXM0NGJpTjQyNTdNYndtZW4zbjFlNzF5ZHZIVytiZDVOM3JZK3VUNlZQbmM4dlh3RGZlZDZudk9UK0NYNmpmQXIvRGZ1Lzl2ZjBML2ZmNS94bmdHcEFYc0RQZzZYaUg4Y0x4VzhkM0Ixb0g4Z0kzQjNZRnNZSXlnMzRJNmdxMkN1WUYxd2MvQ3JFSkVZUnNDM25DZG1MbnNuZXhuNGU2aGNwQ0Q0YSs1Zmh6NW5HT2gyRmhrV0VWWVIzaCt1SEo0VFhoRHlLc0kzSWlHaVA2STcwaTUwUWVqeUpFUlVldGpMckZOZWZ5dVEzYy9naytFK1pOT0JWTmkwNk1yb2wrRk9NY0k0dHBtNGhPbkRCeDljUjdzWGF4a3RpV09CREhqVnNkZHovZUlYNW0vTStUaUpQaUo5Vk9lcHpnbmpBMzRXd2lJM0Y2NHM3RU4wbWhTY3VUN2lZN0ppdVMyMU4wVWpKU0dsTGVwb2FscmtydG1qeDI4cnpKRjlOTTA4UnByZW1rOUpUMGJla0RVOEtuckozU2srR1ZVWjV4YzZyRDFPS3A1NmVaVHN1ZmRtUzZ6blRlOVAyWmhNelV6SjJaSDNseHZIcmVRQlkzcXk2cm44L2hyK00vRTRRSTFnaDZoWUhDVmNJbjJZSFpxN0tmNWdUbXJNN3BGUVdMcWtSOVlvNjRSdndpTnlwM1UrN2J2TGk4N1hsRCthbjVld3JJQlprRmh5VDZranpKcVJrV000cG5kRXBkcE9YU3JwbitNOWZPN0pkRnk3YkpFZmxVZVd1aEFkeXdYMUk0S3I1VlBDd0tLcW90ZWpjclpkYitZcjFpU2ZHbDJjNnpsOHgrVWhKUjh1TWNmQTUvVHZ0Y3E3bUw1ajZjeDU2M2VUNHlQMnQrK3dLYkJXVUxlaFpHTHR5eGlMb29iOUV2cFc2bHEwcGZmNVA2VFZ1WmVkbkNzdTV2STc5dExOY3VsNVhmK2k3Z3UwMkw4Y1hpeFIxTFBKZXNYL0s1UWxCeG9kS3RzcXJ5NDFMKzBndmZ1MzlmL2YzUXN1eGxIY3U5bDI5Y1FWd2hXWEZ6WmZES0hhdjBWcFdzNmw0OWNYWHpHdGFhaWpXdjEwNWZlNzVxWE5XbWRkUjFpblZkMVRIVnJldHQxNjlZLzdGR1ZIT2pOclIyVDUxWjNaSzZ0eHNFRzY1dURObll0TWw4VStXbUR6K0lmN2k5T1hKemM3MTlmZFVXNHBhaUxZKzNwbXc5KzZQdmp3M2JUTGRWYnZ1MFhiSzlhMGZDamxNTlBnME5PODEyTG05RUd4V052YnN5ZGwzWkhiYTd0Y20xYWZNZTVwN0t2V0N2WXUvdlAyWCtkSE5mOUw3Mi9iNzdtdzdZSGFnN3lEaFkwWXcwejI3dWJ4RzFkTFdtdFhZZW1uQ292UzJnN2VEUFkzN2VmdGpxY08wUnd5UExqMUtQbGgwZE9sWnliT0M0OUhqZmlad1QzZTNUMisrZW5IenkrcWxKcHpwT1I1OCtkeWJpek1tejdMUEh6Z1dlTzN6ZS8veWhDNzRYV2k1NlgyeSs1SFhwNEM5ZXZ4enM4TzVvdnV4enVmV0szNVcyenZHZFI2OEdYejF4TGV6YW1ldmM2eGR2eE43b3ZKbDg4L2F0akZ0ZHR3VzNuOTdKdi9QaTE2SmZCKzh1dkVlNFYzRmY5MzdWQTdNSDlmOXkrdGVlTHUrdUl3L0RIbDU2bFBqb2JqZS8rOWx2OHQ4KzlwUTlwait1ZW1MNXBPR3B4OVBEdlJHOVYzNmY4bnZQTSttendiN3lQL1QrcUh2dStQekFueUYvWHVxZjNOL3pRdlppNk9YU1Z5YXZ0cjhlOTdwOUlIN2d3WnVDTjROdks5Nlp2TnZ4M3ZmOTJRK3BINTRNenZwSStsajl5ZWxUMitmb3ovZUdDb2FHcER3WlQ3VVZ3T0JBczdNQmVMa2RBSG9hQUl3cmNQOHdSWDNPVXdtaVBwdXFFUGhQV0gwV1ZJazNBRTN3cHR5dWM0NERzQmNPKzRXUU93UUE1Vlk5S1FTZ25wNGpReVB5YkU4UE5SY05ubmdJNzRhR1hwa0RRR29ENEpOc2FHaHd3OURRcDYwdzJUc0FISitwUGw4cWhRalBCaitFS05FTjQ0eDM0Q3Y1TndyNGY3WVYrd2hxQUFBQUNYQklXWE1BQUJZbEFBQVdKUUZKVWlUd0FBQUJtV2xVV0hSWVRVdzZZMjl0TG1Ga2IySmxMbmh0Y0FBQUFBQUFQSGc2ZUcxd2JXVjBZU0I0Yld4dWN6cDRQU0poWkc5aVpUcHVjenB0WlhSaEx5SWdlRHA0YlhCMGF6MGlXRTFRSUVOdmNtVWdOUzQwTGpBaVBnb2dJQ0E4Y21SbU9sSkVSaUI0Yld4dWN6cHlaR1k5SW1oMGRIQTZMeTkzZDNjdWR6TXViM0puTHpFNU9Ua3ZNREl2TWpJdGNtUm1MWE41Ym5SaGVDMXVjeU1pUGdvZ0lDQWdJQ0E4Y21SbU9rUmxjMk55YVhCMGFXOXVJSEprWmpwaFltOTFkRDBpSWdvZ0lDQWdJQ0FnSUNBZ0lDQjRiV3h1Y3pwbGVHbG1QU0pvZEhSd09pOHZibk11WVdSdlltVXVZMjl0TDJWNGFXWXZNUzR3THlJK0NpQWdJQ0FnSUNBZ0lEeGxlR2xtT2xCcGVHVnNXRVJwYldWdWMybHZiajQwUEM5bGVHbG1PbEJwZUdWc1dFUnBiV1Z1YzJsdmJqNEtJQ0FnSUNBZ0lDQWdQR1Y0YVdZNlVHbDRaV3haUkdsdFpXNXphVzl1UGpROEwyVjRhV1k2VUdsNFpXeFpSR2x0Wlc1emFXOXVQZ29nSUNBZ0lDQThMM0prWmpwRVpYTmpjbWx3ZEdsdmJqNEtJQ0FnUEM5eVpHWTZVa1JHUGdvOEwzZzZlRzF3YldWMFlUNEtselBiMUFBQUFCeHBSRTlVQUFBQUFnQUFBQUFBQUFBQ0FBQUFLQUFBQUFJQUFBQUNBQUFBVjA0SW1HTUFBQUFqU1VSQlZCZ1pZbng0OTk1L1JrWkdodi8vL3pPQWFNWkg5KzREQlJpQUFneGdBQUFBQVAvLzVWL091UUFBQUNKSlJFRlVZM3g4Ly83Ly8vOFpHQmdaR1JqQTlKUDdELzcvKy84WEtNQU1GZ0FBemtFY2ZsMGJ6U1VBQUFBQVNVVk9SSzVDWUlJPQ\", \"hasOriginal\": false }"
```
Response
```json
{
  "content": [
    {
      "width": 1,
      "height": 1,
      "path": "http://localhost/2019-10-30/edited/5db9425bba193f2d860430da.jpg",
      "format": "jpg",
      "original": false,
      "histories": [
        "crop",
        "rotate"
      ]
    }
  ]
}
```
