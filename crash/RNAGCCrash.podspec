require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = "RNAGCCrash"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.description  = <<-DESC
                  RNAGCCrash
                   DESC
  s.homepage     = "https://developer.huawei.com/consumer/cn/agconnect"
  # brief license entry:
  s.license      = package["license"]
 
  s.platforms    = { :ios => "9.0" }
  s.authors      = { "Huawei AGConnect" => "agconnect@huawei.com" }
  s.source       = { :git => "", :tag => "#{s.version}" }

  s.source_files = "ios/**/*.{h,c,m,swift}"
  s.requires_arc = true
  s.static_framework = true
  s.dependency "React"
  # AGC
  s.dependency "AGConnectCrash","1.9.0.300"
  #HA
  s.dependency "HiAnalytics"
 
end

