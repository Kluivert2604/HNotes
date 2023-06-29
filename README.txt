# HNotes
#Hướng dẫn cài đặt và chạy ứng dụng ghi chú trên Android Studio

#Điều kiện tiên quyết: 
  Tải Android Studio Flamingo: 
    - Hãy tải xuống và cài đặt Android Studio từ trang web chính thức của Google (https://developer.android.com/studio).
    - Theo các hướng dẫn trên trang web, tải xuống bản phù hợp với hệ điều hành của bạn và sau đó cài đặt nó lên máy tính.

Bước 1: Tạo một thư mục mới để chứa dự án
  - Trên máy tính của bạn, tạo một thư mục mới để chứa dự án (không được tạo thư mục ngoài Desktop). 

Bước 2: Sao chép dự án từ GitHub
  - Truy cập vào trang GitHub: https://github.com/Kluivert2604/HNotes.git
  - Nhấp vào nút "Clone or download" và sao chép URL của dự án.

Bước 3: Tải project và cài đặt vào Android Studio Flamingo
  - Vào thư mục mà bạn đã tạo click chuột phải -> Open Terminal -> git clone https://github.com/Kluivert2604/HNotes.git 
  - Sau khi mở Android Studio Flamingo trên máy tính của bạn:
    + Chọn File -> Open -> Chọn thư mục chứa project -> Chọn OK
    + Trong quá trình cài đặt chương trình vào máy gặp lỗi thì bấm chọn File -> Project Structure -> Android Gradle Plugin Version: 8.0.2 
      và Gradle Version 8.1

Bước 4: Chờ Android Studio Flamingo hoàn tất việc sao chép dự án
  - Android Studio Flamingo sẽ cà đặt chương trình
  - Khi quá trình này kết thúc, Android Studio Flamingo sẽ hiển thị giao diện chính của môi trường phát triển.

Bước 5: Xây dựng và chạy chương trình
  - Để chạy được chương trình thì trước tiên phải cài đặt máy ảo Emulator trong Android Studio bằng cách:
    Chọn Device Manager -> Create Device ->Trong cửa sổ Select Hardware: Chọn thiết bị bất kỳ và phù hợp với máy của mình ->
    click Next -> Trong cửa sổ System Image: ở cột Release name thì chọn R là hợp lý nhất -> Next -> Finish
  Ngoài ra, bạn cũng có thể chạy chương trình bằng thiết bị thật của mình (sử dụng hệ điều hành Android): Có thể tham khảo ở đây     
  (https://developer.android.com/codelabs/basic-android-kotlin-compose-connect-device?hl=vi#2)

  - Sau khi cài đặt xong thiết bị để chạy ứng dụng: 
  + Xây dựng dự án bằng cách nhấp vào nút "Build" trên thanh công cụ hoặc sử dụng phím tắt (Ctrl + trên Windows hoặc Command + F9 trên MacOS).
  + Sau khi quá trình xây dựng kết thúc mà không có lỗi, bạn có thể chạy chương trình bằng cách nhấp vào nút "Run" trên thanh công cụ hoặc sử dụng phím tắt (Shift + F10 trên Windows hoặc Control + R trên MacOS).
  + Android Studio Flamingo sẽ tạo và chạy ứng dụng trên thiết bị ảo hoặc thiết bị kết nối.
